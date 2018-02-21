<?php
App::uses('AppController', 'Controller');

/** Api Controller */
class ApiController extends AppController {

/**
 * Controller name
 *
 * @var string
 */
	public $name = 'Api';
	public $scaffold;

	public $uses = array('Document');

	public function beforeRender() {
		$this->layout = 'ajax';
		// $this->layout = 'default';
	}

	public function getDocument($documentId = null) {	
		$this->set('result', $this->Document->api_getById($documentId));
		$this->render('/Elements/json');
    }

	public function getAuthors($documentId = null) {	
		$result = $this->Document->Author->api_getAuthors($documentId);
		$this->set('result', $result);
		// $this->set('_serialize', $result);
		$this->render('/Elements/json');
    }
	
	public function getAuthorInfo($author_id = null) {	
		$result = $this->Document->Author->api_getAuthorInfo($author_id);
		$this->set('result', $result);
		// $this->set('_serialize', $result);
		$this->render('/Elements/json');
    }
	
	// http://localhost/Projekt/trunk/web/Api/getCitations/54
    public function getCitations($documentId = null) {	
		$result = $this->Document->Citation->api_getCitations($documentId);
		$this->set('result', $result);
		// $this->set('_serialize', $result);
		$this->render('/Elements/json');
    }
	
	public function getCitationpatternMembers($documentId = null) {	
		$result = $this->Document->CitationpatternMember->api_getCitationpatternMembers($documentId);
		$this->set('result', $result);
		// $this->set('_serialize', $result);
		$this->render('/Elements/json');
    }
	
	public function getPatterns($documentId = null) {	
		$result = $this->Document->Pattern->api_getPatterns($documentId);
		$this->set('result', $result);
		// $this->set('_serialize', $result);
		$this->render('/Elements/json');
    }

	public function getPatternById($patternId = null) {
		$this->set('result', $this->Document->Pattern->api_getPatternById($patternId));
		$this->render('/Elements/json');
	}
	
	public function getReferences($documentId = null) {	
		$result = $this->Document->Reference->api_getReferences($documentId);
		$this->set('result', $result);
		$this->render('/Elements/json');
    }
	
	public function getText($documentId = null) {	
		$result = $this->Document->Text->api_getText($documentId);
		$this->set('result', $result);
		// $this->set('_serialize', $result);
		$this->render('/Elements/json');
    }
	
	public function getTextpatternMembers($documentId = null) {
		$result = $this->Document->TextpatternMember->api_getTextpatternMembers($documentId);		
		$this->set('result', $result);
		// $this->set('_serialize', $result);
		$this->render('/Elements/json');
    }

}
?>
