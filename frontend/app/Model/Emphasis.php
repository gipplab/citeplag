<?php
App::uses('AppModel', 'Model');
/**
 * Emphasis Model
 *
 */
class Emphasis extends AppModel {

	public $recursive = -1;

	public $belongsTo = array();
	
	public $validate = array(
		'name' => array(
			'notempty' => array(
				'rule' => array('notempty'),
				'message' => 'Select a name',
				//'allowEmpty' => false,
				//'required' => false,
				//'last' => false, // Stop validation after this rule
				//'on' => 'create', // Limit validation to 'create' or 'update' operations
			),
		)
	);
	
	public function getLastAdded() {
		return $this->find('all', array(
			'conditions' => array(),
			'fields' => array(),
			'order' => array('Emphasis.created' => 'desc'),
			'limit' => 5,
		));
	}
	
	public function unsetOldDefault() {
		$oldDefault =  $this->find('first', array(
			'conditions' => array('Emphasis.default' => 1),
		));
		// Set Pointer to old default entry and unset default field
		$this->id = $oldDefault['Emphasis']['id'];
		$this->saveField('default', 0);
	}
	
	public function setFirstToDefault() {
		$newDefault =  $this->find('first', array(
			'conditions' => array('Emphasis.default' => 0),
		));
		// Set Pointer to old default entry and unset default field
		$this->id = $newDefault['Emphasis']['id'];
		$this->saveField('default', 1);
	}
	
	public function setToDefault($id) {
		$oldDefault =  $this->find('first', array(
			'conditions' => array('Emphasis.default' => 1),
		));
		// Set Pointer to old default entry and unset default field
		$this->id = $oldDefault['Emphasis']['id'];
		$this->saveField('default', null);
		// Set Pointer to new default and save default field with 1.
		$this->id = $id;
		$this->saveField('default', 1);
	}
	
	public function getCurrentEmphasis() {
		$currentEmphasis = $this->find('first',array(
			'conditions' => array(
				'Emphasis.default' => 1
			)
		));
		return $currentEmphasis;
	}
	
/** 
*	API functions
**/
	public function api_getEmphases() {
		return $this->find('all', array(
			'conditions' => array(),
			'fields' => array(),
			'order' => array(),
			// 'limit' => 2, // only for testing
		));
	}
}
?>
