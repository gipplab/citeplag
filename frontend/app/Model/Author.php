<?php
App::uses('AppModel', 'Model');
/**
 * Author Model
 *
 * @property Document $Document
 */
class Author extends AppModel {

	public $primaryKey = 'author_id';
	public $displayField = 'lastname';
	public $virtualFields = array(
		// mistake, if lastname == NULL (e.g. id == 80)
	    'fullname' => 'CONCAT(Author.firstname, " ", Author.lastname)'
	);
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
	
	public function getAuthorsByDocumentId($documentId) {
		$authors = $this->find('all', array(
			'conditions' => array('Author.document_id' => $documentId)
		));
		return Set::classicExtract($authors, '{n}.Author.fullname');
	}
	
/** 
*	API functions
 * gets the author(s) for a selected document_id
**/	
	public function api_getAuthors($documentId = null) {	
		$authors = $this->find('all', array(
			'conditions' => array('Author.document_id' => $documentId),
			'fields' => array('Author.author_id', 'Author.fullname'),
			'order' => array('Author.lastname' => 'asc'),
			// 'limit' => 3
		));
		return $authors;
	}
	
	/**
	 * Info about the Author
	 */
	public function api_getAuthorInfo($author_id = null) {	
		$authors = $this->find('all', array(
			'conditions' => array('Author.author_id' => $author_id),
			'fields' => array('Author.document_id', 'Author.fullname'),
			// 'order' => array('Author.lastname' => 'asc'),
			// 'limit' => 3
		));
		return $authors;
	}
}
?>
