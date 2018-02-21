<?php
App::uses('HtmlHelper', 'View/Helper');

class HtmlExtendedHelper extends HtmlHelper {

	public function endTag($name) {
		return '</'.$name.'>';
	}

}
?>