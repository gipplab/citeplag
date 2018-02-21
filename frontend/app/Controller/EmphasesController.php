<?php
App::uses('AppController', 'Controller');
/**
 * Emphases Controller
 *
 * @property Emphase $Emphase
 */
class EmphasesController extends AppController {
		public $uses = array('Emphasis');
		public $paginate = array();

	/**
	 * index method
	 *
	 * @return void
	 */
		public function index() {
			$this->Emphasis->recursive = 0;
			$this->set('emphases', $this->paginate());
		}

	/**
	 * view method
	 *
	 * @param string $id
	 * @return void
	 */
		public function view($id = null) {
			$this->Emphasis->id = $id;
			if (!$this->Emphasis->exists()) {
				throw new NotFoundException(__('Invalid emphasis'));
			}
			$this->set('emphasis', $this->Emphasis->read(null, $id));
		}

	/**
	 * add method
	 *
	 * @return void
	 */
		public function add( $sourceId ) {
			if ($this->request->is('post')) {
				if($this->request->data['Emphasis']['default'] == 1 ){
					$this->Emphasis->unsetOldDefault();
				}
				$this->Emphasis->create();
				if ($this->Emphasis->save($this->request->data)) {
					$this->Session->setFlash(__('The emphasis has been saved'),'default', array( 'class' => 'alert alert-info'));
					$this->redirect('/compare/' .$sourceId);
				} else {
					$this->Session->setFlash(__('The emphasis could not be saved. Please, try again.'));
				}
			}
			$this->layout = 'ajax';
		}

	/**
	 * edit method
	 *
	 * @param string $id
	 * @return void
	 */
		public function edit($sourceId = null, $id = null) {
			$this->Emphasis->id = $id;
			if (!$this->Emphasis->exists()) {
				throw new NotFoundException(__('Invalid emphasis'));
			}
			if ($this->request->is('post') || $this->request->is('put')) {
				if($this->request->data['Emphasis']['default'] == 1 ){
					$this->Emphasis->unsetOldDefault();
				}
				$emphasis = $this->Emphasis->find('first', array(
					'conditions' => array( 'id' => $id)
				));
				if($emphasis['Emphasis']['default'] == 1 && $this->request->data['Emphasis']['default'] == 0 ){
					if ($this->Emphasis->find('count') > 1) {
						$this->Emphasis->setFirstToDefault();
					} else {
						$this->request->data['Emphasis']['default'] = 1;
					}
				}
				if ($this->Emphasis->save($this->request->data)) {
					$this->Session->setFlash(__('The emphasis has been saved'),'default', array( 'class' => 'alert alert-info'));
					$this->redirect('/compare/' .$sourceId);
				} else {
					$this->Session->setFlash(__('The emphasis could not be saved. Please, try again.'));
				}
			} else {
				$this->request->data = $this->Emphasis->read(null, $id);
			}
			$this->layout = 'ajax';
		}

	/**
	 * delete method
	 *
	 * @param string $id
	 * @return void
	 */
		public function delete($sourceId = null, $id = null) {
			if (!$this->request->is('post')) {
				throw new MethodNotAllowedException();
			}
			$this->Emphasis->id = $id;
			if (!$this->Emphasis->exists()) {
				throw new NotFoundException(__('Invalid emphasis'));
			}
			$emphasis = $this->Emphasis->find('first', array(
				'conditions' => array( 'id' => $id)
			));
			if($emphasis['Emphasis']['default'] == 1 ){
				if($this->Emphasis->find('count') > 1) {
					$this->Emphasis->setFirstToDefault();
				}
			}
			$amount = $this->Emphasis->find('count');
			if($amount > 1) {
				$this->Emphasis->id = $id;
				if ($this->Emphasis->delete()) {
					$this->Session->setFlash(__('Emphasis deleted'),'default', array( 'class' => 'alert alert-info'));
					$this->redirect('/compare/' .$sourceId);
				}
			} else {
				$this->Session->setFlash(__('You cannot delete the last Emphasis'),'default', array( 'class' => 'alert alert-error'));
				$this->redirect('/compare/' .$sourceId);	
			}
		}
}
