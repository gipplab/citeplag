<?php
	$meta = array();
	
	$date = false;
	if(isset($document['Document']['month']) && isset($document['Document']['year'])) {
		$date = new DateTime();
		$date = $this->Time->format('Y-m', $date->setDate($document['Document']['year'], $document['Document']['month'], 1));
	} else if (!isset($document['Document']['month']) && isset($document['Document']['year'])) {
		$date = $document['Document']['year'];
	} else {
		$date = false;
	}
	
	array_push($meta, $this->Html->tag('span', $date, array('class' => 'muted')));
	
	if(isset($document['Document']['pubmed'])) {
		array_push($meta, $this->Html->link(__('in PubMed'), 'http://www.ncbi.nlm.nih.gov/pubmed/' . $document['Document']['pubmed'], array('target' => '_blank')));
	}
	
	if(isset($document['Document']['pmc'])) {
		array_push($meta, $this->Html->link(__('in PMC'), 'http://www.ncbi.nlm.nih.gov/pmc/articles/PMC' . $document['Document']['pmc'], array('target' => '_blank')));
	}

	echo implode('&nbsp;|&nbsp;', $meta);
	
	if($document['Document']['title']) {
		echo $this->Html->tag('h1', $document['Document']['title']);	
	}
	if(is_array($document['Author']) && !empty($document['Author'])) {
		echo $this->Html->tag('span', String::toList($document['Author']), array('class' => 'muted'));
	}
?>