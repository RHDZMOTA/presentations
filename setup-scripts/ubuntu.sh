#!/bin/bash

# Install git & jq
sudo apt install -y git jq

# Install python
sudo apt install -y python3.7

# Install java 8
sudo apt install -y openjdk-8-jdk

# Install pandoc
sudo apt install -y pandoc pandoc-citeproc

# Install R & Dependencies
sudo apt install -y r-base
sudo apt install -y libcurl4-openssl-dev libssl-dev libxml2-dev r-cran-xml
echo "install.packages(c('rmarkdown', 'tinytex', 'reticulate'))" | R --vanilla --quiet
echo "install.packages('devtools', dependencies = TRUE, repos = 'http://cran.us.r-project.org')" | R --vanilla --quiet
echo "devtools::install_github('cboettig/knitcitations')" | R --vanilla --quiet

# Install pdflatex
sudo apt install -y texlive-latex-base texlive-fonts-recommended texlive-fonts-extra texlive-latex-extra

# Install SBT
sudo apt install wget
wget https://dl.bintray.com/sbt/debian/sbt-1.2.7.deb
sudo dpkg -i sbt-1.2.7.deb
rm -r sbt-1.2.7.deb
sbt about

# Install Ruby and Jekyll
sudo apt install ruby ruby-dev build-essential
export GEM_HOME=$HOME/gems
export PATH=$HOME/gems/bin:$PATH
gem install jekyll bundler

