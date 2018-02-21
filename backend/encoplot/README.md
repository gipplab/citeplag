# Encoplot

For detecting text matches, CitePlag uses Encoplot by Christian Grozea, which is a light-weight, fast, and accurate text-based detection tool. Encoplot itself is a small C script. The code for Encoplot is avalaible in this [paper](http://ceur-ws.org/Vol-502/paper2.pdf)

# How to use

The python-script "encoplot_similarity.py" uses this (modified) encoplot. It is called in line 11 of the script and has to match the path to this binary.
