<?php
App::uses('AppModel', 'Model');
/**
 * Document Model
 *
 * @property Document $Document
 * @property DocReference $DocReference
 * @property DbReference $DbReference
 */
class Document extends AppModel {

	public $useTable = 'document_data';
	public $primaryKey = 'document_id';
	public $recursive = -1;

	public $hasMany = array(
		'Author' => array(
			'className' => 'Author',
			'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'Reference' => array(
			'className' => 'Reference',
			'foreignKey' => 'cont_document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'Citation' => array(
			'className' => 'Citation',
			'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'Pattern' => array(
			'className' => 'Pattern',
			'foreignKey' => 'document_id1',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'CitationpatternMember' => array(
			'className' => 'CitationpatternMember',
			'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
		'Text' => array(
			'className' => 'Text',
			'foreignKey' => 'document_id',
			'conditions' => '',
			'fields' => '',
			'order' => ''
		),
	);

	public function getById($documentId) {
		$result = $this->find('all', array(
			'conditions' => array('Document.document_id' => $documentId),
		));
		$result = Set::combine($result, '{n}.Document.type', '{n}.Document.value');
		$result['id'] = $documentId;
		return $result;
	}
	
	public function getByIds($documentIds) {
		$results = array();
		$documents = $this->find('all', array(
			'conditions' => array('Document.document_id' => $documentIds),
		));
		foreach ($documents as $key => $document) {
			$results[$document['Document']['document_id']][$document['Document']['type']] = $document['Document']['value'];
		}
		foreach ($results as $key => $result) {
			$results[$key]['document_id'] = $key;
		}
		return $results;
	}
	
	public function getWithAssociatedDataById($documentId, $citationPatternIds = null, $textPatternIds = null) {
		$result = array(
			'Document' => $this->getById($documentId),
			'Author' => $this->Author->getAuthorsByDocumentId($documentId),
			'Citation' => $this->Citation->getByCitationsDocumentId($documentId),
			'Reference' => $this->Reference->getReferencesByDocumentId($documentId),
			'Text' => $this->Text->getByDocumentId($documentId)
		);
		$result['CitationpatternMember'] = $this->Pattern->CitationpatternMember->getByPatternIds($citationPatternIds);
		$result['TextpatternMember'] = $this->Pattern->TextpatternMember->getByPatternIds($documentId, $textPatternIds);
		return $result;
	}
	
	public function getDocumentId($type, $value) {
		return $this->field( 'Document.document_id', array(
			'Document.type' => $type,
			'Document.value' => $value
		));
	}
	
	/** 
	*	API functions
	**/	
	public function api_getById($documentId) {
		$result = $this->find('all', array(
			'conditions' => array('Document.document_id' => $documentId),
		));
		return Set::combine($result, '{n}.Document.type', '{n}.Document.value');
	}
	
}
?>
