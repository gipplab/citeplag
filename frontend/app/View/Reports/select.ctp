<?php $this->set('title_for_layout', "Welcome to CitePlag");?>
<div style="height: 100%; overflow: auto;">
<div class="title">Welcome to CitePlag</div>

<div class="controls-startpage">
<?php 
	echo $this->Html->link("Upload Documents", array('controller' =>'pages', 'action' => 'display','document_upload'), array( 'class' => 'btn btn-inverse btn-primary btn-startpage'));
	echo '<div class="spacer-startpage"></div>';
	echo $this->Html->link("See Recent Findings", array('controller' =>'pages', 'action' => 'display','recent_findings'), array( 'class' => 'btn btn-inverse btn-primary btn-startpage'));
	echo '<div class="spacer-startpage"></div>';
	echo $this->Html->link("Compare PubMed Central Documents", array('controller' =>'pages', 'action' => 'display','pmcoas_select'), array( 'class' => 'btn btn-inverse btn-primary btn-startpage'));
?>
</div>

<div class="description-title">CitePlag demonstrates Citation-based Plagiarism Detection (CbPD)</div>
<p class="description">CbPD is  a novel approach to identifying plagiarism by analyzing citation pattern matches between documents to detect both local and global document similarities at the semantic level. For details on CbPD see our <a href="http://sciplore.org/projects/citation-based-plagiarism-detection">related publications</a> or the doctoral thesis of <a href="https://gipplab.org/team/prof-dr-bela-gipp">Bela Gipp</a>.</p>
<p class="description">If you have any questions please <a href="mailto:team@sciplore.org">contact us.</a></p>

<script type="text/javascript" charset="utf-8">
	cbpdp.init();
	cbpdp.events.init();
</script>
