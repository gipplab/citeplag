<?php
App::uses('Pattern', 'Model');

class ReporterHelper extends AppHelper {
	public $helpers = array('Html', 'HtmlExtended');
	
	private $pos = 0;
	private $depth = 0;
	private $output = '';
	private $anchors = array();
	private $nextPos = NULL;
	private $nextAnchor = NULL;
	private $pattern = array();
	private $references = array();
	private $openMark = NULL;
	private $currentMarks = array();
	private $buf = '';
	
	private function defaultHandler($parser, $data) {
		$this->buf .= $data;
		$this->pos += mb_strlen(html_entity_decode($data));
	}

	private function cdataHandler($parser, $data) {
		$len = mb_strlen($data);
		$pos = 0;
		while ($this->nextPos !== NULL && $this->pos + ($len - $pos) >= $this->nextPos) {
			$currentText = htmlspecialchars(mb_substr($data, $pos, $this->nextPos - $this->pos));
			if ($this->openMark && !$this->openMark['open'] && mb_strlen(trim($currentText))) {
				$this->buf .= $this->openMark['html'];
				$this->openMark['open'] = true;
			}
			$this->buf .= $currentText;
			foreach ($this->currentMarks as &$v) {
				$v .= $currentText;
			}
			foreach ($this->nextAnchor as $a) {
				switch($a['type']) {
					case 'citation':
						$this->buf .= $this->__renderCitation($a);
						break;
					case 'textstart':
						if ($this->openMark) {
							$this->buf .= $this->HtmlExtended->endTag('mark');
							$this->openMark = NULL;
						}
						$this->openMark = $a;
						$this->openMark['html'] = $this->__renderMark($a);
						$this->openMark['open'] = true;
						$this->buf .= $this->openMark['html'];
						$this->currentMarks[$a['pattern_id']] = '';
						break;
					case 'textend':
						$this->injectPopup();
						unset($this->currentMarks[$a['pattern_id']]);
						if ($this->openMark && $this->openMark['open'] == true && $this->openMark['pattern_id'] == $a['pattern_id']) {
							$this->buf .= $this->HtmlExtended->endTag('mark');
						}
						if ($this->openMark && $this->openMark['pattern_id'] == $a['pattern_id']) {
							$this->openMark = NULL;
						}
						break;
				}
				$this->flush();
			}
			$pos += $this->nextPos - $this->pos;
			$this->pos = $this->nextPos;
			$this->next();
		}
		$currentText = htmlspecialchars(mb_substr($data, $pos));
		if ($this->openMark && !$this->openMark['open'] && mb_strlen(trim($currentText))) {
			$this->buf .= $this->openMark['html'];
			$this->openMark['open'] = true;
		}
		$this->buf .= $currentText;
		foreach($this->currentMarks as &$v) {
			$v .= $currentText;
		}
		$this->pos += $len - $pos;
	}

	private function startElementHandler($parser, $name, $attribs) {
		if ($this->openMark && $this->openMark['open']) {
			$this->buf .= $this->HtmlExtended->endTag('mark');
			$this->openMark['open'] = false;
		}
		switch($name) {
			case 'XML':
				return;
			case 'TITLE':
				if ($this->depth >= 1 && $this->depth <= 6) {
					$this->buf .= '<h' . $this->depth . '>';
				} else if ($this->depth < 1) {
					$this->buf .= '<h1>';
				} else {
					$this->buf .= '<h6>';
				}
				return;
			case 'SECTION':
				$this->depth++;
			default:
				$this->buf .= '<' . strtolower($name);
		}
		foreach ($attribs as $k => $v) {
			$this->buf .= ' ' . strtolower($k) . '="' . $v . '"';
		}
		$this->buf .= '>';
		$this->flush();
	}

	private function endElementHandler($parser, $name) {
		if ($this->openMark && $this->openMark['open']) {
			$this->buf .= $this->HtmlExtended->endTag('mark');
			$this->openMark['open'] = false;
		}
		switch($name) {
			case 'XML':
				return;
			case 'TITLE':
				if ($this->depth >= 1 && $this->depth <= 6) {
					$this->buf .= '</h' . $this->depth . '>' . "\n";
				} else if ($this->depth < 1) {
					$this->buf .= '</h1>' . "\n";
				} else {
					$this->buf .= '</h6>' . "\n";
				}
				return;
			case 'SECTION':
				$this->depth--;
			case 'P':
				$this->buf .= "\n";
			default:
				$this->buf .= '</' . strtolower($name) . '>';
		}
		$this->flush();
	}
	
	private function next() {
		reset($this->anchors);
		$this->nextPos = key($this->anchors);
		if ($this->nextPos !== NULL) {
			$this->nextAnchor = $this->anchors[$this->nextPos];
			unset($this->anchors[$this->nextPos]);
		} else {
			$this->nextAnchor = NULL;
		}
	}

	private function flush($force = false) {
		$this->injectPopup();
		if (empty($this->currentMarks) || $force) {
			$this->output .= $this->buf;
			$this->buf = '';
		}
	}

	private function injectPopup() {
		foreach($this->currentMarks as $k=>&$v) {
			$this->buf = preg_replace('/<mark data-pattern_id="(' . $k . ')" (.*?) data-complete=".*?" (.*?)>/', '<mark data-pattern_id="$1" $2 data-complete="' . preg_replace('/\s+/', ' ', $v) . '" $3>', $this->buf);
		}
	}

	/**
	 * @author Mario Lipinski
	 */
	public function renderText($document, $pattern, $options = array()) {
		// FIXME BEGIN
		$this->pos = 0;
		$this->depth = 0;
		$this->output = '';
		$this->anchors = array();
		$this->nextPos = NULL;
		$this->nextAnchor = NULL;
		$this->pattern = array();
		$this->references = array();
		$this->openMark = NULL;
		$this->currentMarks = array();
		$this->buf = '';
		// FIXME END
	
		foreach ($pattern['Pattern'] as $p) {
			$this->pattern[$p['pattern_id']] = $p;
		}
		
		$citations = array();
		$document['Citation'] = array_reverse($document['Citation']);
		foreach ($document['Citation'] as $c) {
			$c = $c['Citation'];
			$c['patterns'] = array();
			$citations[$c['db_citation_id']] = $c;
		}
		
		foreach ($document['CitationpatternMember'] as $m) {
			$m = $m['CitationpatternMember'];
			if (isset($citations[(int)$m['db_citation_id']])) { // FIXME: This is total crap, document[CitationpatternMember] should not contain Members for other documents
				$citations[(int)$m['db_citation_id']]['patterns'][] = $m;
			}
		}
		
		foreach ($document['Reference'] as $r) {
			$this->references[$r['db_reference_id']] = $r;
		}

		foreach ($document['TextpatternMember'] as $k => $m) {
			$m = $m['TextpatternMember'];
			if (!isset($this->anchors[$m['end_character']])) {
				$this->anchors[$m['end_character']] = array();
			}
			$this->anchors[$m['end_character'] + 1][] = array_merge(array('type' => 'textend'), $m);
		}
		
		foreach ($citations as $c) {
			if (!isset($this->anchors[$c['character']])) {
				$this->anchors[$c['character']] = array();
			}
			$this->anchors[$c['character']][] = array_merge(array('type' => 'citation'), $c);
		}

		foreach ($document['TextpatternMember'] as $k => $m) {
			$m = $m['TextpatternMember'];
			if (!isset($this->anchors[$m['start_character']])) {
				$this->anchors[$m['start_character']] = array();
			}
			$this->anchors[$m['start_character']][] = array_merge(array('type' => 'textstart', 'length' => ($m['end_character'] - $m['start_character'] + 1)), $m);
		}
		
		ksort($this->anchors);

		$this->next();
	
		$text = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE citeplag [
	<!ENTITY nbsp "&#160;">
]>
<xml>' . $document['Text']['Text']['fulltext'] . '</xml>';
		$xml = xml_parser_create();
		xml_set_object($xml, $this);
		xml_set_character_data_handler($xml, 'cdataHandler');
		xml_set_element_handler($xml, 'startElementHandler', 'endElementHandler');
		xml_set_default_handler($xml, 'defaultHandler');
		$this->output .= "\n";
		if (!xml_parse($xml, $text, true)) {
			debug(xml_error_string(xml_get_error_code($xml)));
		}
		$this->flush(true);
		$this->output .= "\n";
		xml_parser_free($xml);
		return $this->output;
	}

	/**
	 * @author André Gernandt
	 */
	public function getProcedureKey($id) {
		$data = array(
			Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE => 'lccs',
			Pattern::TYPE_LONGEST_COMMON_CITATION_SEQUENCE_DISTINCT => 'lccsdist',
			Pattern::TYPE_GREEDY_CITATION_TILING_SHARED =>  'gct',
			Pattern::TYPE_GREEDY_CITATION_TILING_ALL => 'gct',
			Pattern::TYPE_GREEDY_CITATION_TILING_ALL_MATCH_SHARED_REFS => 'gct',
			Pattern::TYPE_BIBLIOGRAPHIC_COUPLING => 'bc',
			Pattern::TYPE_ONE_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE => 'cc',
			Pattern::TYPE_ONE_DOC_SHINGLED_ADJACENT_CIT_MERGE => 'cc',
			Pattern::TYPE_ONE_DOC_SHINGLED_DEP_PREDECESSOR_NO_MERGE => 'cc',
			Pattern::TYPE_ONE_DOC_SHINGLED_DEP_PREDECESSOR_MERGE => 'cc',
			Pattern::TYPE_ONE_DOC_SHINGLED_WEIGHTED_NO_MERGE => 'cc',
			Pattern::TYPE_ONE_DOC_SHINGLED_WEIGHTED_MERGE => 'cc',
			Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_NO_MERGE => 'cc',
			Pattern::TYPE_BOTH_DOC_SHINGLED_ADJACENT_CIT_MERGE => 'cc',
			Pattern::TYPE_BOTH_DOC_SHINGLED_DEP_PREDECESSOR_NO_MERGE => 'cc',
			Pattern::TYPE_BOTH_DOC_SHINGLED_DEP_PREDECESSOR_MERGE => 'cc',
			Pattern::TYPE_BOTH_DOC_SHINGLED_WEIGHTED_NO_MERGE => 'cc',
			Pattern::TYPE_BOTH_DOC_SHINGLED_WEIGHTED_MERGE => 'cc',
		);
		return $data[$id];
	}
	
	/**
	 * @author André Gernandt
	 */
	private function __renderMark($m) {
		return $this->Html->tag('mark', null, array(
			'data-pattern_id' => $m['pattern_id'],
			'data-length' => $m['length'],
			'data-complete' => '...',
			'class' => 'h'.($m['pattern_id'] % 12)
			// 'class' => 'textmatch'
		));
	}

	/**
	 * @author André Gernandt
	 */
	private function __renderCitation($c) {
		if (isset($this->references[$c['db_reference_id']])) {
			$reference = $this->references[$c['db_reference_id']];
		} else {
			$referecen = NULL;
		}
		
		$options = array(
			'data-citation_id' => $c['db_citation_id']
			// 'data-reference_id' => $reference['db_reference_id']
		);
		if (!empty($c['patterns'])) {
			$cssClasses = array( 'match' );
			$identifiers = array();
			$procedures = array();
			foreach ($c['patterns'] as $m) {
				array_push($cssClasses, $this->getProcedureKey($this->pattern[$m['pattern_id']]['procedure']), 'procedure_'.$this->pattern[$m['pattern_id']]['procedure']);
				array_push($identifiers, array(
					'procedure' => $this->pattern[$m['pattern_id']]['procedure'],
					'pattern_id' => $m['pattern_id'],
					'count' => $m['count'],
				));
				$procedures[] = $this->pattern[$m['pattern_id']]['procedure'];
			}
			$options['class'] = implode(' ', array_unique($cssClasses));
			$options['data-identifier'] = json_encode($identifiers);
			$options['data-procedure'] = json_encode(array_values(array_unique($procedures)));
		}
		
		if (array_key_exists('Document', $reference)) {
			$options['data-document_id'] = $reference['Document']['document_id'];
		}
		
		// FIXME
		// @TODO
		// experimental rewriting of citation captions
		$splittedCaption = $caption = explode('-', $reference['doc_reference_id']);
		$caption = "";
		if (count($splittedCaption) > 1) {
			$caption = array_pop($splittedCaption);
		} else {
			$caption = array_pop($splittedCaption);
			$caption = substr($caption, strlen(preg_replace('/\d+$/', '', $caption)));
		}
		// experimental end
		$caption = $reference['doc_reference_id'];
		
		$result = $this->Html->tag('cite', $caption, $options);
		return $result;
	}
}
