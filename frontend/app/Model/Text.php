<?php
App::uses('AppModel', 'Model');
/**
 * Document Model
 *
 * @property Document $Document
 * @property DocReference $DocReference
 * @property DbReference $DbReference
 */
class Text extends AppModel {

	public $useTable = 'document_text';
	public $primaryKey = 'document_id';
	public $recursive = -1;

	public $belongsTo = array(
		'Document' => array(
			'className' => 'Document',
			'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		)
	);
	
	/** 
	* API functions
 	* gets the text from a selected document_id
	**/	
	public function api_getText($documentId = null) {	
		$text = $this->find('first', array(
			'conditions' => array('Text.document_id' => $documentId),
			'fields' => array('Text.fulltext'),
			// 'order' => array('Author.lastname' => 'asc'),
		));
		return $text;
	}
	
	public function getByDocumentId($documentId) {
		return $this->find('first', array(
			'conditions' => array('Text.document_id' => $documentId)
		));
	}
}
?>
