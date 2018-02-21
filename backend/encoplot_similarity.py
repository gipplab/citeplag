#!/usr/bin/env python
# -*- coding: utf-8 -*-
import json
import os
import random
import re
import subprocess
import sys
import tempfile

ENCOPLOT = '/usr/local/bin/encoplot'
NGRAM_LENGTH=16

def run_encoplot(input):
	p = subprocess.Popen(ENCOPLOT, stdin=subprocess.PIPE, stdout=subprocess.PIPE);
	(stdout, stderr) = p.communicate(input="\n".join(input))
	if stderr:
		print stderr
	return stdout

def process_output(outstring):
	outstring = outstring.replace("\r", "");
	splitted = outstring.split("\n\n\n")
	output = {}
	output['summary'] = splitted[-1].strip()
	output['parts'] = splitted[:-1]
	return output

def postprocess_summary(raw):
	raw = raw.replace("\r", "");
	splitted = raw.split("\n\n\n")
	data = splitted[-1].strip().split('\n')
	res = {}
	res['first'] = int(data[0].split('  ')[0].split()[0].strip())
	res['firstu'] = int(data[0].split('  ')[0].split()[1].strip())
	res['second'] = int(data[1].split('  ')[1].split()[0].strip())
	res['secondu'] = int(data[1].split('  ')[1].split()[1].strip())
	res['comp'] = int(data[0].split('  ')[1].split()[0].strip())
	res['compu'] = int(data[0].split('  ')[1].split()[1].strip())
	if max(res['firstu'], res['secondu']) == 0:
		res['score'] = 0
	else:
		res['score'] = int((float(res['compu']) / min(res['firstu'], res['secondu'])) * 100)
	return res
	
def postprocess_output(raw):
	rawstructured = []
	raw = raw.replace('\r', '');
	for l in raw.split("\n"):
		r = l.split("\t")
		if len(r) < 5:
			continue
		rawstructured.append({'pos_a': int(r[0]), 'pos_b': int(r[1]), 'ngram_nr': int(r[2]), 'ngram_hex': r[3], 'ngram_string': r[4]})
	rawstructured = sorted(rawstructured, key=lambda k: k['pos_a'])

	lpos_a = None
	lpos_b = None
	match = None
	matches = []
	i = 0
	for l in rawstructured:
		if lpos_a == None or l['pos_a'] != lpos_a+1 or l['pos_b'] != lpos_b+1:
			if lpos_a != None:
				matches.append(match)
			match = {'idx': i, 'pos_a_start': None, 'pos_a_end': None, 'pos_b_start': None, 'pos_b_end': None, 'string': None, 'ngrams': [], 'length': NGRAM_LENGTH}
			match['pos_a_start'] = l['pos_a']
			match['pos_a_end'] = l['pos_a']+NGRAM_LENGTH-1
			match['pos_b_start'] = l['pos_b']
			match['pos_b_end'] = l['pos_b']+NGRAM_LENGTH-1
			match['string'] = l['ngram_string']
			#match['ngrams'].append(l['ngram_nr'])
			i += 1
		else:
			match['pos_a_end'] = l['pos_a']+NGRAM_LENGTH-1
			match['pos_b_end'] = l['pos_b']+NGRAM_LENGTH-1
			match['length'] += 1
			match['string'] += l['ngram_string'][-1]
			#match['ngrams'].append(l['ngram_nr'])
		lpos_a = l['pos_a']
		lpos_b = l['pos_b']
	matches.append(match)
	return matches;

def do():
	if len(sys.argv) != 3:
		print "Invalid arguments.";
		return;
	filename1 = sys.argv[1]
	filename2 = sys.argv[2]
	output = run_encoplot((filename1, filename2))
	output = process_output(output)
	matches = postprocess_output(output['parts'][1])
	summary = postprocess_summary(output['summary'])
	pattern = [];
	textpattern = ([], []);
	pattern.append({'procedure': 50, 'pattern_score': summary['score']})
	for m in matches:
		id = hash(m['pos_a_start'])+hash(m['pos_a_end'])+hash(m['pos_b_start'])+hash(m['pos_b_end'])+random.randint(1, 10000000)
		pattern.append({'pattern_id': id, 'procedure': 51, 'pattern_score': m['length']})
		textpattern[0].append({'pattern_id': id, 'start_character': m['pos_a_start'], 'end_character': m['pos_a_end']})
		textpattern[1].append({'pattern_id': id, 'start_character': m['pos_b_start'], 'end_character': m['pos_b_end']})
	print json.dumps({'pattern': pattern, 'textpattern_doc1': textpattern[0], 'textpattern_doc2': textpattern[1]})

do()
