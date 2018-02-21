<?php
App::uses('AppModel', 'Model');
/**
 * Reference Model
 *
 */
class Reference extends AppModel {

	public $useTable = 'reference';
	public $primaryKey = 'db_reference_id';
	public $recursive = -1;

	public $belongsTo = array(
		'Document' => array(
			'className' => 'Document',
			'foreignKey' => 'cont_document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		// document_data table is ambigious because references itself belong to a document and the document where a reference is pointing to are stored in the same table 
		// this source belonging shows up the document where the reference is pointing to
		'Source' => array(
			'className' => 'Document',
			'foreignKey' => 'ref_document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
	);
	
	public $hasMany = array(
		'Citation' => array(
			'className' => 'Citation',
			'foreignKey' => 'db_reference_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
	);
	
	public function getReferencesByDocumentId($documentId) {
		$references = $this->Document->Reference->find('all', array(
			'conditions' => array('Reference.cont_document_id' => $documentId),
			'fields' => array('Reference.db_reference_id', 'Reference.ref_document_id', 'Reference.doc_reference_id'),
		));
		$references = Set::combine($references, '{n}.Reference.db_reference_id', '{n}.Reference');
		if ($references) {
			$documentsForReferences = $this->Document->getByIds(array_unique(Set::classicExtract($references,'{n}.ref_document_id')));
			foreach ($references as $referenceId => $reference) {
				if (array_key_exists($reference['ref_document_id'], $documentsForReferences)) {
					$references[$referenceId]['Document'] = $documentsForReferences[$reference['ref_document_id']];
				}
			}
		}
		return $references;
	}
	
	/** 
	*	API functions
	**/	
	public function api_getReferences($documentId = null) {
		$references = $this->find('all', array(
			'conditions' => array('Reference.cont_document_id' => $documentId),
			'fields' => array('Reference.db_reference_id', 'Reference.doc_reference_id', 'Reference.ref_document_id'),
			'order' => array(),
			// 'limit' => 1 // only for testing
		));
		return $references;
	}
	
	public function api_getReferencesByDocumentId($documentId) {
		return $this->getReferencesByDocumentId($documentId);
	}	

}
?>
