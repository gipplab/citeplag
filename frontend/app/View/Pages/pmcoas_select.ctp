<?php
	$this->extend('page');
	$this->set('title_for_layout', "Compare PubMed Central Documents");
?>
<div class="title">Compare PubMed Central Documents</div>
<div class="row">
	<?php echo $this->Form->create('Report', array('action' => 'select', 'type' => 'post', 'class' => 'form-horizontal')); ?>
	<div class="span5">
		<fieldset>
				<div class="description-title">Select Document 1</div>
			<div class="control-group">
				<label class="control-label">
					<?php echo $this->Form->select('source_db', array('pubmed' => __('PMID'), 'pmc' => __('PMCID')), array('empty' => false)); ?>
				</label>
				<?php echo $this->Form->input('source_id', array('type' => 'number', 'value' => 20195930, 'placeholder' => 'e.g. 20195930', 'div' => 'controls', 'label' => false)); ?>
			</div>
	</div>
	<div class="span5">
			<div class="description-title">Select Document 2</div>
			<div class="control-group">
				<label class="control-label">
					<?php echo $this->Form->select('comparison_db', array('pubmed' => __('PMID'), 'pmc' => __('PMCID')), array('empty' => false)); ?>
				</label>
				<?php echo $this->Form->input('comparison_id', array('type' => 'number', 'placeholder' => 'e.g. 20058113', 'div' => 'controls', 'label' => false)); ?>
			</div>				
	</div>
		<div class="submit">
		<input type="submit"  value="Load Documents" style="margin-top:10px;" class ="btn btn-inverse btn-primary pull-right">
	</div>
	<?php echo $this->Form->end(); ?>
</div>
<div>
	<h5 style="font-size:16px; margin-top:20px;">You can select documents from the PubMed Central Open Access Subset (PMC OAS).</h5>
	<p class="description"><a href="http://www.ncbi.nlm.nih.gov/pmc/?term=open+access[filter]">This is a list of articles included in the PMC Open Access Subset</a>. Because the PMC OAS is expanded continiously, CitePlag does not index all articles yet. Please use the PubMed Central ID (PMCID) or, if available, the PubMed ID (PMID)  to specifiy the documents you want to check using CitePlag!</p>
</div>