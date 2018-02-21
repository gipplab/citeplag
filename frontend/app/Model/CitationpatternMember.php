<?php
App::uses('AppModel', 'Model');
/**
 * CitationpatternMember Model
 *
 * @property PatternMember $PatternMember
 * @property Pattern $Pattern
 * @property ContDocument $ContDocument
 * @property DbCitation $DbCitation
 */
class CitationpatternMember extends AppModel {

	public $useTable = 'citationpattern_member';
	public $primaryKey = 'pattern_member_id';
	public $recursive = -1;

	public $belongsTo = array(
		'Document' => array(
			'className' => 'Document',
			'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'Pattern' => array(
			'className' => 'Pattern',
			'foreignKey' => 'pattern_id',
			'conditions' => '',
			'fields' => '',
			'order' => '',
		),
		'Citation' => array(
			'className' => 'Citation',
			'foreignKey' => 'db_citation_id',
			'conditions' => '',
			'fields' => '',
			'order' => '',
		),
	);
	
	public function getByPatternIds($patternIds) {
		return $this->find('all', array(
			'conditions' => array(
				'CitationpatternMember.pattern_id' => $patternIds
			),
			'fields' => array('CitationpatternMember.pattern_id', 'CitationpatternMember.db_citation_id', 'CitationpatternMember.pattern_member_id', 'CitationpatternMember.count')
		));
	}
	
/** 
*	API functions
**/
	public function api_getCitationpatternMembers($documentId = null) {
		$citationpatternMembers = $this->find('all', array(
			'conditions' => array('CitationpatternMember.document_id' => $documentId),
			'order' => array(),
			'fields' => array('pattern_member_id', 'pattern_id', 'count', 'gap', 'db_citation_id'),
			'recursive' => -1
		));
		return $citationpatternMembers;
	}

}
?>