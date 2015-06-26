# Google Document Translator #

A free, open source translator which uses Google's translation power to translate a document and preserves the format.

<font color='red'> <b>Disclaimer</b> : Google Document Translator is not a product from Google. Neither it is endorsed nor it is supported by Google. This is a free and open source product, distributed WITHOUT ANY WARRANTY.<br>
<br>
<b>Note</b> : Google language api is a paid service. Use free translation for academic purpose only. For professional usage, please get a api key from Google.</font>

Google provides a web interface to translate a document. But the constraint is, the translated document will be shown as a web-page and you can not get the translated document in your original binary format. This backlog prompts us to develop a desktop application which will exploit Google's translation power to translate a document, right from your desktop. Currently this software supports the below file formats:
  * doc
  * xls
  * ppt
  * docx
  * xlsx
  * pptx
  * txt

## Usage ##
<p>Google Document translator is written in java and requires <a href='http://www.oracle.com/technetwork/java/javase/downloads/index.html'>jre</a> to run. Please install <a href='http://www.oracle.com/technetwork/java/javase/downloads/index.html'>jre 1.6.0</a> before downloading this software. After installation, open console in your system, type "java" and press enter. If the console prints the usage and options, then your system is ready to run this software.</p>

<p>Download the latest release from the <a href='http://code.google.com/p/google-document-translator/downloads/list'>Downloads</a> link and unzip it. Inside you will find Gdt.bat/Gdt.sh which is the executable for windows/linux.</p>

### Bug Report ###
<p>This project is still in its early phase of development and we expect some errors to be reported by the users. Any bugs/query/suggestion can be raised through the <a href='http://code.google.com/p/google-document-translator/issues/list'>issues</a> link. Please attach the log (which can be found in the same directory where the software is unzipped) and the necessary steps to reproduce the bug. Also, it will be quite helpful, if the document, which is failed to translate, is also attached along with the bug report.</p>

This project uses
  * [apache poi](http://poi.apache.org/) to handle microsoft office documents
  * [google-api-translate-java](http://code.google.com/p/google-api-translate-java/) to call google language api from a java client
  * [dom4j](http://dom4j.sourceforge.net/) for xml parsing
  * [jaxen](http://jaxen.codehaus.org/) for xpath
  * [itext](http://itextpdf.com/) for pdf support ( in Progress )