<?php
	$this->extend('page');
   $this->set('title_for_layout', "Document Upload");
?>
<div class="title">Document Upload <span class="beta">(beta)</span></div>
<?php echo $this->element('document_select'); ?>
<div>
	<h5 style="font-size:16px; margin-top:20px;">You can upload pdf or txt files for comparison.</h5>
	<p class="description">This feature is still in beta. Currently, CitePlag can only parse a limited number of citation styles. On-demand processing is computationally demanding. For two 10-page documents expect processing times of approx. 10 seconds, depending on the server load and your internet connection.</p>
	<h5 style="font-size:16px; margin-top:20px;">Example files</h5>
	<p class="description">If you don't have suitable documents available, you can use these two example files representing a case of translated plagiarism from Chinese to English.
<ul>
 <li><?php echo $this->Html->link('File Chinese', 'https://www.dropbox.com/s/onfx6x8jhpypqog/1_Chinesisch.pdf'); ?>, <?php echo $this->Html->link('File English', 'https://www.dropbox.com/s/2c9npua6tsflgyf/2_Englisch.pdf'); ?></li>
</ul>
	
</div>