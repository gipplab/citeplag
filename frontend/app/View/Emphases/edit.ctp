<button type="button" class="close" id="btnEmphasisEditCancel" aria-hidden="true">×</button>
<div class="emphases form">
<?php echo $this->Form->create('Emphasis', array('class' => 'form-horizontal')); ?>
	<fieldset>
		<legend><?php echo __('Edit Emphasis'); ?></legend>
		<?php
			echo $this->Form->input('id'); ?>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">Name</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('name', array('div' => 'controls', 'class' => 'input-small','label' => false)); ?>
		</div>
		<b>Similarity</b>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">BC</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_bc', array('div' => 'controls', 'data-bc' => '', 'class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">CC</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_cc', array('div' => 'controls', 'data-cc' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">GCT</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_gct', array('div' => 'controls', 'data-gct' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">LCCS</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_lccs', array('div' => 'controls', 'data-lccs' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">LCCS Dist.</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_lccs_dist', array('div' => 'controls', 'data-lccs-dist' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">Encoplot</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_encoplot', array('div' => 'controls', 'data-encoplot' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<b>Recommendation</b>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">CPA</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_cpa', array('div' => 'controls', 'data-cpa' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">BC (Global)</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_bcg', array('div' => 'controls', 'data-bcg' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">Co-Citation</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_cocit', array('div' => 'controls', 'data-cocit' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">Lucene</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->input('coefficient_lucene', array('div' => 'controls', 'data-lucene' => '','class' => 'input-small','label' => false)); ?>
		</div>
		<div class="control-group">
			<?php echo $this->Html->tag('label', '<span class="label">Set as Default</span>', array('class'=>'control-label', 'escape' => false));?>
			<?php echo $this->Form->checkbox('default', array('div' => 'controls', 'class' => 'input-small','label' => false)); ?>
		</div>
	</fieldset>
<?php echo $this->Form->end(array('label' => __('Save'), 'id' => 'btnEmphasisEditSave', 'class' => 'btn btn-small pull-right', 'disabled' => false)); ?>
</div>
