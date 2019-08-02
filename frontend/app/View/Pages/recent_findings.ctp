<?php
	$this->extend('page');
	$this->set('title_for_layout', "Recent Findings");
?>
<div class="title">Recent Findings</div>
	<div class="description-title">Translation Plagiarism:</div>
<ul>
	<li><?php echo $this->Html->link('Doctoral thesis of the former German defence minister (Guttenberg) - translation plagiarism, high citation and low text similarity', '/compare/6861131?substpat_a=(%25)&substpat_b=(%25)'); ?>
	<br/> (We have some issues with our database. The comparison of this document can therefore lead to a timeout.)</li>
<!--	<li><?php echo $this->Html->link('Translation plagiarism Chinese to English, high citation and no text similarity', '/compare/6861270/6861269'); ?></li>-->
	
</ul>
	<div class="description-title">Retracted studies: </div>
<ul>
	<li><?php echo $this->Html->link('Retracted medical study 1', '/compare/5583/117324'); ?></li>
	<li><?php echo $this->Html->link('Retracted medical study 2', '/compare/4727/43777'); ?></li>
	<li><?php echo $this->Html->link('Retracted medical study 3', '/compare/18399/13772'); ?></li>
</ul>
	<div class="description-title">Most recently viewed documents:</div>
<ul>
	<li><?php echo $this->Html->link('Recent 1', '/compare/104895/155283'); ?></li>
	<li><?php echo $this->Html->link('Recent 2', '/compare/110389/136117'); ?></li>
	<li><?php echo $this->Html->link('Recent 3', '/compare/139453/16480'); ?></li>
	<li><?php echo $this->Html->link('Recent 4', '/compare/28816/21031'); ?></li>
	<li><?php echo $this->Html->link('Recent 5', '/compare/125263/122587'); ?></li>
	<li><?php echo $this->Html->link('Recent 6', '/compare/76634/19305'); ?></li>    
	<li><?php echo $this->Html->link('Recent 7', '/compare/178041/69910'); ?></li>
</ul>
