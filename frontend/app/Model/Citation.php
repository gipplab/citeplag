<?php
App::uses('AppModel', 'Model');
/**
 * Citation Model
 *
 * @property Document $Document
 * @property DocReference $DocReference
 * @property DbReference $DbReference
 */
class Citation extends AppModel {

	public $useTable = 'citation';
	public $primaryKey = 'db_citation_id';
	public $recursive = -1;

	public $belongsTo = array(
		'Document' => array(
			'className' => 'Document',
			'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'Reference' => array(
			'className' => 'Reference',
			'foreignKey' => 'db_reference_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		)
	);
	
	public function getByCitationsDocumentId($documentId) {
		$citations = $this->find('all', array(
			'conditions' => array('Citation.document_id' => $documentId),
			'fields' => array('Citation.db_citation_id', 'Citation.document_id', 'Citation.db_reference_id', 'Citation.count', 'Citation.character'),
			// to order by both fields is maybe not needed as long as the parser build up character values in right order for multi-value citations
			'order' => array('Citation.character' => 'desc', 'Citation.count' => 'desc')
		));
		return $citations;
	}
	
/** 
*	API functions
**/
	public function api_getCitations($documentId = null) {
		return $this->getByCitationsDocumentId($documentId);
	}
}
?>
