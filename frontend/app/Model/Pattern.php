<?php
App::uses('AppModel', 'Model');
/**
 * Pattern Model
 *
 * @property PatternMember $PatternMember
 */
class Pattern extends AppModel {

	const TYPE_LONGEST_COMMON_CITATION_SEQUENCE = 1;
	const TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT = 11;

	const TYPE_GREEDY_CITATION_TILING_SHARED = 2;
	const TYPE_GREEDY_CITATION_TILING_ALL = 21;
	const TYPE_GREEDY_CITATION_TILING_ALL_MATCH_SHARED_REFS = 22;
	
	const TYPE_ONE_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE = 30;
	const TYPE_ONE_DOC_SHINGLED_ADJACENT_CIT_MERGE = 31;
	const TYPE_ONE_DOC_SHINGLED_DEP_PREDECESSOR_NO_MERGE = 32;
	const TYPE_ONE_DOC_SHINGLED_DEP_PREDECESSOR_MERGE = 33;
	const TYPE_ONE_DOC_SHINGLED_WEIGHTED_NO_MERGE = 34;
	const TYPE_ONE_DOC_SHINGLED_WEIGHTED_MERGE = 35;

	const TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE = 40;
	const TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_MERGE = 41;
	const TYPE_BOTH_DOC_SHINGLED_DEP_PREDECESSOR_NO_MERGE = 42;
	const TYPE_BOTH_DOC_SHINGLED_DEP_PREDECESSOR_MERGE = 43;
	const TYPE_BOTH_DOC_SHINGLED_WEIGHTED_NO_MERGE = 44;
	const TYPE_BOTH_DOC_SHINGLED_WEIGHTED_MERGE = 45;
	
	const TYPE_ENCOPLOT_GLOBAL = 50;
	const TYPE_ENCOPLOT_PATTERN = 51;
	
	const TYPE_CPA = 60;
	
	const TYPE_BIBLIOGRAPHIC_COUPLING_GLOBAL = 70;
	const TYPE_BIBLIOGRAPHIC_COUPLING = 71;

	const TYPE_COCIT = 80;
	
	const TYPE_LUCENE = 90;
	
	public $useTable = 'pattern';
	public $primaryKey = 'pattern_id';
	public $recursive = -1;

	public $belongsTo = array(
		'Document' => array(
			'className' => 'Document',
			'foreignKey' => 'document_id1',
			// 'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
	);

	public $hasMany = array(
		'CitationpatternMember' => array(
			'className' => 'CitationpatternMember',
			'foreignKey' => 'pattern_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'TextpatternMember' => array(
			'className' => 'TextpatternMember',
			'foreignKey' => 'pattern_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		)
	);
	
	public function getCitationProdeduresWithFullScope() {
		return array(
			Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE, Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT, Pattern::TYPE_GREEDY_CITATION_TILING_ALL, Pattern::TYPE_BIBLIOGRAPHIC_COUPLING, Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE
		);
	}
	
	public function getTextProdeduresWithFullScope() {
		return array(
			Pattern::TYPE_ENCOPLOT_PATTERN
		);
	}
	
	public function getCitationPatternByDocumentIds($documentId1, $documentId2) {
		$patterns = $this->find('all', array(
			'conditions' => array(
				'OR' => array(
					array(
						'Pattern.document_id1' => $documentId1,
						'Pattern.document_id2' => $documentId2,
					),
					array(
						'Pattern.document_id1' => $documentId2,
						'Pattern.document_id2' => $documentId1,
					)
				),
				'Pattern.procedure' => $this->getCitationProdeduresWithFullScope()
			),
			'fields' => array('Pattern.pattern_id', 'Pattern.procedure', 'Pattern.pattern_score'),
		));
		return Set::combine($patterns, '{n}.Pattern.pattern_id', '{n}.Pattern');
	}
	
	public function getTextPatternByDocumentIds($documentId1, $documentId2, $minLength = 0) {
		$patterns = $this->find('all', array(
			'conditions' => array(
				'OR' => array(
					array(
						'Pattern.document_id1' => $documentId1,
						'Pattern.document_id2' => $documentId2,
					),
					array(
						'Pattern.document_id1' => $documentId2,
						'Pattern.document_id2' => $documentId1,
					)
				),
				'Pattern.procedure' => $this->getTextProdeduresWithFullScope(),
				'Pattern.pattern_score >=' => $minLength
			),
			'fields' => array('Pattern.pattern_id', 'Pattern.procedure', 'Pattern.pattern_score'),
		));
		return Set::combine($patterns, '{n}.Pattern.pattern_id', '{n}.Pattern');
	}
	
	public function getDocumentsforCluster($documentId = null, $algorithmEmphasis = array()) {
		$coefficients = array();
		if(!empty($algorithmEmphasis) && count(array_filter($algorithmEmphasis)) > 0) {
			$coefficients[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING] = (isset($algorithmEmphasis['coefficient_bc']) && !is_null($algorithmEmphasis['coefficient_bc'])) ? $algorithmEmphasis['coefficient_bc'] : 0;
			$coefficients[Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE] = (isset($algorithmEmphasis['coefficient_cc']) && !is_null($algorithmEmphasis['coefficient_cc'])) ? $algorithmEmphasis['coefficient_cc'] : 0;
			$coefficients[Pattern::TYPE_GREEDY_CITATION_TILING_ALL] = (isset($algorithmEmphasis['coefficient_gct']) && !is_null($algorithmEmphasis['coefficient_gct'])) ? $algorithmEmphasis['coefficient_gct'] : 0;
			$coefficients[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE] = (isset($algorithmEmphasis['coefficient_lccs']) && !is_null($algorithmEmphasis['coefficient_lccs'])) ? $algorithmEmphasis['coefficient_lccs'] : 0;
			$coefficients[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT] = (isset($algorithmEmphasis['coefficient_lccs_dist']) && !is_null($algorithmEmphasis['coefficient_lccs_dist'])) ? $algorithmEmphasis['coefficient_lccs_dist'] : 0;
			$coefficients[Pattern::TYPE_ENCOPLOT_PATTERN] = (isset($algorithmEmphasis['coefficient_encoplot']) && !is_null($algorithmEmphasis['coefficient_encoplot'])) ? $algorithmEmphasis['coefficient_encoplot'] : 0;
			$coefficients[Pattern::TYPE_CPA] = (isset($algorithmEmphasis['coefficient_cpa']) && !is_null($algorithmEmphasis['coefficient_cpa'])) ? $algorithmEmphasis['coefficient_cpa'] : 0;
			$coefficients[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING_GLOBAL] = (isset($algorithmEmphasis['coefficient_bcg']) && !is_null($algorithmEmphasis['coefficient_bcg'])) ? $algorithmEmphasis['coefficient_bcg'] : 0;
			$coefficients[Pattern::TYPE_COCIT] = (isset($algorithmEmphasis['coefficient_cocit']) && !is_null($algorithmEmphasis['coefficient_cocit'])) ? $algorithmEmphasis['coefficient_cocit'] : 0;
			$coefficients[Pattern::TYPE_LUCENE] = (isset($algorithmEmphasis['coefficient_lucene']) && !is_null($algorithmEmphasis['coefficient_lucene'])) ? $algorithmEmphasis['coefficient_lucene'] : 0;
		} else {
			if (array_key_exists('coefficent_cpa' , $algorithmEmphasis)) {
				$coefficients[Pattern::TYPE_CPA] = !is_null($algorithmEmphasis['coefficent_cpa']) ? $algorithmEmphasis['coefficent_cpa'] : 0;
			} else {
				#$coefficients = $this->getDefaultCoefficients();
			}
		}
		$dbo = $this->getDataSource();
		$subq1 = $dbo->buildStatement(
			array(
				'table' => $dbo->fullTableName($this),
				'alias' => 'p',
				'conditions' => array('document_id1' => $documentId),
				'fields' => array('document_id2', '`procedure`', 'pattern_score')
			),
			$this
		);
		$subq2 = $dbo->buildStatement(
			array(
				'table' => $dbo->fullTableName($this),
				'alias' => 'p',
				'conditions' => array('document_id2' => $documentId),
				'fields' => array('document_id1', '`procedure`', 'pattern_score')
			),
			$this
		);
		$query = 'SELECT * FROM (' . $subq1 . ' UNION ' . $subq2 . ') Pattern ORDER BY `pattern_score` DESC';
		$documents = $dbo->fetchAll($query);
		if(!empty($documents) && is_array($documents)) {
			$scoresForDocuments = array();
			foreach($documents as $document) {
				if(!isset($scoresForDocuments[$document['Pattern']['document_id2']])) {
					$scoresForDocuments[$document['Pattern']['document_id2']] = 0;
				}
				// $scoresForDocuments[$document['Pattern']['document_id2']] += $document['Pattern']['pattern_score'];
				if(array_key_exists($document['Pattern']['procedure'], $coefficients)) {
					$scoresForDocuments[$document['Pattern']['document_id2']] += ($document['Pattern']['pattern_score'] * $coefficients[$document['Pattern']['procedure']] );
				}
			}
			arsort($scoresForDocuments);
			return $scoresForDocuments;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the document id for the document which has the highest score in comparioson
	 */
	public function findHighestRankedComparisonDocument($sourceId, $algorithmEmphasis = array()) {
		$documents = $this->getDocumentsforCluster($sourceId, $algorithmEmphasis);
		if(is_numeric(key($documents))) {
			return(key($documents));
		} 
		return false;
	}
	
	public function getAlgorithmPercentage($algorithmEmphasis = array()) {
		$coefficients = array();
		if(empty($algorithmEmphasis)) {
			$coefficients = $this->getDefaultCoefficients();
		} else {
			unset($algorithmEmphasis['source_id']);
			$coefficients[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING] = $algorithmEmphasis['coefficient_bc'];
			$coefficients[Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE] = $algorithmEmphasis['coefficient_cc'];
			$coefficients[Pattern::TYPE_GREEDY_CITATION_TILING_ALL] = $algorithmEmphasis['coefficient_gct'];
			$coefficients[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE] = $algorithmEmphasis['coefficient_lccs'];
			$coefficients[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT] = $algorithmEmphasis['coefficient_lccs_dist'];
			$coefficients[Pattern::TYPE_ENCOPLOT_PATTERN] = $algorithmEmphasis['coefficient_encoplot'];
			$coefficients[Pattern::TYPE_CPA] = $algorithmEmphasis['coefficient_cpa'];
			$coefficients[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING_GLOBAL] = $algorithmEmphasis['coefficient_bcg'];
			$coefficients[Pattern::TYPE_COCIT] = $algorithmEmphasis['coefficient_cocit'];
			$coefficients[Pattern::TYPE_LUCENE] = $algorithmEmphasis['coefficient_lucene'];
		}
		$percentages = array();
		$sum = array_sum($coefficients);
		if ($sum == 0) {$sum = 1; }
		foreach($coefficients as $type => $coefficient) {
			$percentages[$type] = ($coefficient / $sum) * 100;
		}
		return($percentages);
	}
	
	public function getDefaultCoefficients() {
		App::import('model', 'Emphasis');
		$Emphasis = new Emphasis();
		$defaultCoefficients = $Emphasis->find('first', array(
			'conditions' => array('Emphasis.default' => 1)
		));
		$coefficients[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING] = $defaultCoefficients['Emphasis']['coefficient_bc'];
		$coefficients[Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE] = $defaultCoefficients['Emphasis']['coefficient_cc'];
		$coefficients[Pattern::TYPE_GREEDY_CITATION_TILING_ALL] = $defaultCoefficients['Emphasis']['coefficient_gct'];
		$coefficients[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE] = $defaultCoefficients['Emphasis']['coefficient_lccs'];
		$coefficients[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT] = $defaultCoefficients['Emphasis']['coefficient_lccs_dist'];
		$coefficients[Pattern::TYPE_ENCOPLOT_PATTERN] = $defaultCoefficients['Emphasis']['coefficient_encoplot'];
		$coefficients[Pattern::TYPE_CPA] = $defaultCoefficients['Emphasis']['coefficient_cpa'];
		$coefficients[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING_GLOBAL] = $defaultCoefficients['Emphasis']['coefficient_bcg'];
		$coefficients[Pattern::TYPE_COCIT] = $defaultCoefficients['Emphasis']['coefficient_cocit'];
		$coefficients[Pattern::TYPE_LUCENE] = $defaultCoefficients['Emphasis']['coefficient_lucene'];
		return $coefficients;
	}
	
	/** 
	*	API functions
	**/	
	public function api_getPatterns($documentId1 = null, $documentId2) {
		// TODO
	}
	
	public function api_getPatternById($patternId = null) {
		$pattern = $this->find('first', array(
			'conditions' => array('pattern_id' => $patternId),
			'fields' => array('Pattern.pattern_id', 'Pattern.procedure', 'Pattern.pattern_score'),
			'contain' => array(
				'CitationpatternMember' => array(
					'Citation' => array(
						'Reference' => array(
							'Source'=> array(
								'Author'
							)
						)
					)
				)
			)
		));
		return $pattern;
	}
	
	public function getAllAlgorithms() {
		$algorithms = array(
			Pattern::TYPE_BIBLIOGRAPHIC_COUPLING => array( 'title' =>'BC', 'fieldname' => 'coefficient_bc', 'name' => 'Bibliographic Coupling Strength'),
			Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE => array('title' =>'CC', 'fieldname' => 'coefficient_cc', 'name' => 'Citation Chunking'),
			Pattern::TYPE_GREEDY_CITATION_TILING_ALL => array( 'title' =>'GCT', 'fieldname' => 'coefficient_gct', 'name' => 'Greedy Citation Tiling'),
			Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE => array( 'title' =>'LCCS', 'fieldname' => 'coefficient_lccs', 'name' => 'Longest Common Citation Sequence'),
			Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT => array( 'title' =>'LCCS Dist', 'fieldname' => 'coefficient_lccs_dist', 'name' => 'Longest Common Citation Sequence Distinct'),
			Pattern::TYPE_ENCOPLOT_PATTERN => array( 'title' =>'ENC', 'fieldname' => 'coefficient_encoplot', 'name' => 'Encoplot'),
			Pattern::TYPE_CPA => array( 'title' =>'CPA', 'fieldname' => 'coefficient_cpa', 'name' => 'Co-Citation Proximity Analysis'),
			Pattern::TYPE_BIBLIOGRAPHIC_COUPLING_GLOBAL => array( 'title' =>'BCG', 'fieldname' => 'coefficient_bcg', 'name' => 'Bibliographic Coupling (global)'),
			Pattern::TYPE_COCIT => array( 'title' =>'COCIT', 'fieldname' => 'coefficient_cocit', 'name' => 'Co-Citation'),
			Pattern::TYPE_LUCENE => array( 'title' =>'LUCENE', 'fieldname' => 'coefficient_lucene', 'name' => 'Lucene MoreLikeThis'),
		);
		return $algorithms;
	}

	public function getRecommendationAlgorithmFieldnames() {
		$all = $this->getAllAlgorithms();
		$algorithms = array(
			$all[Pattern::TYPE_CPA]['fieldname'],
			$all[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING_GLOBAL]['fieldname'],
			$all[Pattern::TYPE_COCIT]['fieldname'],
			$all[Pattern::TYPE_LUCENE]['fieldname'],
		);
		return $algorithms;
	}

	public function getSimilarityAlgorithmFieldnames() {
		$all = $this->getAllAlgorithms();
		$algorithms = array(
			$all[Pattern::TYPE_BIBLIOGRAPHIC_COUPLING]['fieldname'],
			$all[Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE]['fieldname'],
			$all[Pattern::TYPE_GREEDY_CITATION_TILING_ALL]['fieldname'],
			$all[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE]['fieldname'],
			$all[Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT]['fieldname'],
			$all[Pattern::TYPE_ENCOPLOT_PATTERN]['fieldname'],
		);
		return $algorithms;
	}

}
?>
