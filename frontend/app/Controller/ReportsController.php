<?php
App::uses('AppController', 'Controller');
App::uses('Folder', 'Utility');
App::uses('File', 'Utility');

/**
 * Reports Controller
 *
 * 
 */

class ReportsController extends AppController {

/**
 * Controller name
 *
 * @var string
 */
	public $name = 'Reports';

/**
 * This controller uses the Document model
 *
 * @var array
 */
	public $uses = array('Document');
	// public $modelClass = 'Document';
	
	private function __initSettings() {
		if (!$this->Session->check('cbpdp.settings')) {
			$this->Session->write('cbpdp.settings', array(
				'highlight_text' => '1',
				'highlight_citation' => '1',
				'highlight_matchingcitation' => '0',
				'show_connection' => '1',
				'show_documentbrowser' => '1',
				'encoplot_keylength' => '16',
				'citation_replacement_pattern_source' => false,
				'citation_replacement_pattern_comparison' => false
			));
		}
	}
	
	/**
 	* This action loads all the necessary Document data for the initial report view
 	*/
	public function compare($sourceId = null, $comparisonId = null) {
		$this->__initSettings();
		$this->loadModel('Emphasis');
		if (!$sourceId) {
			$this->redirect(array('controller' => 'reports', 'action' => 'select'));
		}
		$algorithmEmphasis = array();
		if ($this->request->is('post')) {
			if(isset($this->request->data['Report']['emphasis_id'])) {
				$emphasis = $this->Emphasis->find('first', array(
					'conditions' => array('Emphasis.id' => $this->request->data['Report']['emphasis_id'])
				));
				$algorithmEmphasis = $emphasis['Emphasis'];
			} else {
				$algorithmEmphasis = $this->request->data['Report'];
			}
		}
    if (empty($algorithmEmphasis)) {
			if ($this->Session->check('emphasis')) {
				$algorithmEmphasis = $this->Session->read('emphasis');
			} else {
				$algorithmEmphasis = $this->Emphasis->getCurrentEmphasis()['Emphasis'];
			}
    }
		$this->Session->write('emphasis', $algorithmEmphasis);
		if ($comparisonId === null) {
			$comparisonId = $this->Document->Pattern->findHighestRankedComparisonDocument($sourceId, $algorithmEmphasis);
		}

		$this->set(compact('sourceId', 'comparisonId', 'algorithmEmphasis'));
	}
	
	public function setSettings($sourceId = null, $comparisonId = null) {
		if ($this->request->is('post')) {
			if (!is_numeric($this->request->data['Report']['encoplot_keylength'])) {
				$this->request->data['Report']['encoplot_keylength'] = 0;
			}
			
			if (strlen($this->request->data['Report']['citation_replacement_pattern_source']) != 3 || strpos($this->request->data['Report']['citation_replacement_pattern_source'], '%') != 1) {
				$this->request->data['Report']['citation_replacement_pattern_source'] = false;
			}
			
			if (strlen($this->request->data['Report']['citation_replacement_pattern_comparison']) != 3 || strpos($this->request->data['Report']['citation_replacement_pattern_comparison'], '%') != 1) {
				$this->request->data['Report']['citation_replacement_pattern_comparison'] = false;
			}
			
			$this->Session->write('cbpdp.settings', $this->request->data['Report']);
			$this->redirect(array('controller' => 'reports', 'action' => 'compare', $this->request->data['Report']['source_id'], $this->request->data['Report']['comparison_id']));
		}
		$this->redirect(array('controller' => 'reports', 'action' => 'compare'));
	}

	/**
	 * @DEPRECATED due to data is read from DB
	 */
	private function __getFileContent($fileName) {
		$path = WWW_ROOT . 'test_files' . DS . $fileName;
		$file = new File($path);
		$contents = $file->read();
		$contents = str_replace("\n", " ", $contents);
		$file->close();
		return $contents;
	}
	
	public function select() {
		if ($this->request->is('post')) {
			$data = $this->request->data['Report'];
			$sourceId = $this->Document->getDocumentId($data['source_db'], $data['source_id']);
			if ($sourceId) {
				if ($comparisonId = $this->Document->getDocumentId($data['comparison_db'], $data['comparison_id'])) {
					$this->redirect(array('action' => 'compare', $sourceId, $comparisonId));
				} else {
					$comparisonId = $this->Document->Pattern->findHighestRankedComparisonDocument($sourceId);
					$this->redirect(array('action' => 'compare', $sourceId, $comparisonId));
				}
			} else {
				$this->Session->setFlash(__('No Document ID ' . $data['source_id'] . ' in ' . $data['source_db']), 'flash_error');
			}
		} else {
		}
	}
	
	public function upload() {
		$this->__initSettings();
		if ($this->request->is('post')) {
			$data = $this->data['Report'];

			$p = proc_open('java -cp /opt/CbPD\ Backend/bin:/opt/CbPD\ Backend/lib/collections-generic-4.01.jar:/opt/CbPD\ Backend/lib/guava-r07.jar:/opt/CbPD\ Backend/lib/eclipselink.jar:/opt/CbPD\ Backend/lib/commons-lang3-3.1.jar org.sciplore.cbpd.main.Handler ' . $data['source_file']['tmp_name'] . ' ' . $data['comparison_file']['tmp_name'], array(1=>array('pipe', 'w'), 2=>array('pipe', 'w')), $pipes, NULL, array('LANG' => 'en_US.UTF-8'));
			
			$output = stream_get_contents($pipes[1]);
			$error = stream_get_contents($pipes[2]);
			$r = json_decode($output, true);
			$ret = proc_close($p);

			if ($ret) {
				$e = new InternalErrorException("An error occured while parsing the documents.\n" . $error);
				throw $e;
			} elseif (!empty($error)) {
                $this->Session->setFlash(nl2br($error), 'flash_error');
            }
			
			$source = $this->__postProcessResponseDocument($r['doc1']);
			$comparison = $this->__postProcessResponseDocument($r['doc2']);
			$pattern = $this->__postProcessResponsePattern($r['pattern']);
			$algorithmEmphasis = array();
		} else {
				$this->Session->setFlash(__('Method not supported.'), 'flash_error');
		}
		$this->set(compact('source', 'comparison', 'pattern', 'algorithmEmphasis'));
	}
	
	private function __postProcessResponseDocument($doc) {
		$data = array();
		foreach($doc['data'] as $d) {
			$data[$d['type']] = $d['value'];
		}
		$doc['Document'] = $data;
		
		$citations = array();
		foreach ($doc['Citation']['Citation'] as $c) {
			$citations[] = array('Citation' => $this->__intToStringArray($c));
		}
		$doc['Citation'] = array_reverse($citations);
		
		$references = array();
		foreach ($doc['Reference'] as $r) {
			$references[$r['db_reference_id']] = $this->__intToStringArray($r);
		}
		$doc['Reference'] = $references;
		
		$citationpatternMembers = array();
		foreach ($doc['CitationpatternMember']['CitationpatternMember'] as $m) {
			$citationpatternMembers[] = array('CitationpatternMember' => $this->__intToStringArray($m));
		}
		$doc['CitationpatternMember'] = $citationpatternMembers;
		
		$textpatternMembers = array();
		foreach ($doc['TextpatternMember']['TextpatternMember'] as $m) {
			$textpatternMembers[] = array('TextpatternMember' => $this->__intToStringArray($m));
		}
		$doc['TextpatternMember'] = array_reverse($textpatternMembers);
		
		return $doc;
	}
	
	private function __postProcessResponsePattern($pattern) {
		$npattern = array();
		foreach ($pattern as $p) {
			$npattern[] = $this->__intToStringArray($p);
		}
		
		return array('Pattern' => $npattern);
	}	
	
	private function __intToStringArray($a) {
		foreach (array_keys($a) as $f) {
			if (gettype($a[$f]) == "integer") {
				$a[$f] = (string)$a[$f];
			}
		}
		return $a;
	}
	
	private function __getDocumentsForComparisonByUrl($sourceId = null, $comparisonId = null) {
		if ($sourceId == null) {
			$keys = array_keys($this->params['named']);			
			if (count($this->params['named']) > 0) {
				$sourceId = $this->Document->getDocumentId(array('type' => $keys[0], 'value' => $this->params['named'][0]));
			} else if (count($this->params['named']) == 1) {
				$comparisonId = $this->Document->Pattern->findHighestRankedComparisonDocument($sourceId);
			} else if (count($this->params['named']) == 2) {
				$comparisonId = $this->Document->getDocumentId(array('type' => $keys[1], 'value' => $this->params['named'][1]));
			} else {
				return false;
			}
		} else {
			if ($comparisonId == null) {
				$comparisonId = $this->Document->Pattern->findHighestRankedComparisonDocument($sourceId);
			} else {
				// $this->compare($sourceId, $comparisonId)
			}
		}
		return array('sourceId' => $sourceId, 'comparisonId' => $comparisonId);
	}
	
	public function cluster($sourceId = null) {
		$algorithmEmphasis = array();
		$activeEmphasisId = null;
		if ($this->request->is('post')) {
			$algorithmEmphasis = $this->request->data;
			if(!empty($algorithmEmphasis)) {
				$this->Session->write('cbpdp.cluster.emphasis', $algorithmEmphasis);
			}
			if($this->Session->read('cbpdp.cluster.emphasis')) {
				if(empty($algorithmEmphasis)) {
					$algorithmEmphasis = $this->Session->read('cbpdp.cluster.emphasis');					
				}
				if($this->Session->check('cbpdp.cluster.emphasis.id')){
					$activeEmphasisId = $this->Session->read('cbpdp.cluster.emphasis.id');
				}
			}
		}

		$algorithmPercentages = $this->Document->Pattern->getAlgorithmPercentage($algorithmEmphasis);
		$documentsForCluster = $this->Document->Pattern->getDocumentsforCluster($sourceId, array_intersect_key($algorithmEmphasis, array_fill_keys($this->Document->Pattern->getSimilarityAlgorithmFieldnames(), true)));
		$cpaDocumentsForCluster = $this->Document->Pattern->getDocumentsforCluster($sourceId, array_intersect_key($algorithmEmphasis, array_fill_keys($this->Document->Pattern->getRecommendationAlgorithmFieldnames(), true)));
		if(is_array($documentsForCluster) && !empty($documentsForCluster)) {
			foreach($documentsForCluster as $key => $score) {
				$documentsData[$key] = $this->Document->getById($key);
			}
		}
		if(is_array($cpaDocumentsForCluster) && !empty($cpaDocumentsForCluster)) {
			foreach($cpaDocumentsForCluster as $key => $score) {
				$cpaDocumentsData[$key] = $this->Document->getById($key);
			}
		}
		$algorithms = $this->Document->Pattern->getAllAlgorithms();
		$this->loadModel('Emphasis');
		$emphasesList = $this->Emphasis->find('list');
		if(empty($activeEmphasisId)) {
			$activeEmphasis = $this->Emphasis->getCurrentEmphasis();
			$activeEmphasisId = $activeEmphasis['Emphasis']['id'];
		}
		$this->set(compact('documentsForCluster', 'cpaDocumentsForCluster', 'sourceId', 'documentsData', 'cpaDocumentsData', 'algorithms', 'algorithmEmphasis', 'emphasesList', 'algorithmPercentages', 'activeEmphasisId'));
		$this->layout = 'ajax';
	}
	
	public function renderDocuments( $sourceId = null, $comparisonId = null ) {
		$this->debug_to_console("renderDocuments");
		$citationPattern = $this->Document->Pattern->getCitationPatternByDocumentIds($sourceId, $comparisonId);
		$textPattern = $this->Document->Pattern->getTextPatternByDocumentIds($sourceId, $comparisonId, $this->Session->read('cbpdp.settings.encoplot_keylength'));
		$pattern['Pattern'] = array_merge($citationPattern, $textPattern);

		$source = $this->Document->getWithAssociatedDataById($sourceId, array_keys($citationPattern), array_keys($textPattern));
		$comparison = $this->Document->getWithAssociatedDataById($comparisonId, array_keys($citationPattern), array_keys($textPattern));

		
		if(!empty($algorithmEmphasis)) {
			$algorithmEmphasis = json_encode($algorithmEmphasis);
		}
		$this->set(compact('source', 'comparison', 'pattern', 'algorithmEmphasis'));
		$this->layout = 'ajax';
	}

	/**
	 * Simple helper to debug to the console
	 *
	 * @param  Array, String $data
	 * @return String
	 */
	function debug_to_console( $data ) {
		if ( is_array( $data ) )
			$output = "<script>console.log( 'Debug Objects: " . implode( ',', $data) . "' );</script>";
		else
			$output = "<script>console.log( 'Debug Objects: " . $data . "' );</script>";

		echo $output;
	}
}
?>
