<div class="row">
	<?php echo $this->Form->create('Report', array('action' => 'upload', 'type' => 'file', 'class' => 'form-horizontal')); ?>
	<div class="span5">
		<fieldset>
			<div class="description-title">Select Suspicious Document</div>
			<div class="control-group">
				<?php echo $this->Form->input('source_file', array('type' => 'file', 'div' => 'controls', 'label' => false)); ?>
			</div>
		</fieldset>
	</div>
	<div class="span5">
		<fieldset>
			<div class="description-title">Select Comparison Document</div>
			<div class="control-group">
				<?php echo $this->Form->input('comparison_file', array('type' => 'file', 'div' => 'controls', 'label' => false)); ?>
			</div>				
		</fieldset>
		</div>
	<div class="submit">
		<input type="submit"  value="Upload Documents" style="margin-top:10px;" class ="btn btn-inverse btn-primary pull-right">
	</div>
	<?php echo $this->Form->end(); ?>
</div>