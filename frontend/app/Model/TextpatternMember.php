<?php
App::uses('AppModel', 'Model');
/**
 * TextpatternMember Model
 *
 * @property Document $Document
 */
class TextpatternMember extends AppModel {

	public $useTable = 'textpattern_member';
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
	);
	
	public function getByPatternIds($documentId, $patternIds) {
		return $patternMembers = $this->find('all', array(
			'conditions' => array(
				'TextpatternMember.pattern_id' => $patternIds,
				'TextpatternMember.document_id' => $documentId
			),
			'fields' => array('TextpatternMember.pattern_id', 'TextpatternMember.start_character', 'TextpatternMember.end_character'),
			'order' => array('TextpatternMember.start_character' => 'desc')
		));
	}
	
	/** 
	*	API functions
	**/	
	public function api_getTextpatternMembers($documentId = null) {
		$textpatternMembers = $this->find('all', array(
			'conditions' => array('TextpatternMember.document_id' => $documentId),
			'order' => array(),
			// 'limit' => 3
		));
		return $textpatternMembers;
	}
}
?>
