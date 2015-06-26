## Troubleshoot ##

These are some common problem you may face :
<p><b>1. I have downloaded and unzipped the software, but nothing is running when I click on Gdt.bat</b>
<ul><li><b>Cause</b> You may not have java installed in your system. Or even if you have java in your system, it is not accessible from console or it is an older version<br>
</li><li><b>Solution</b> open a console. In windows, open "Run", type "cmd", press enter. Now in console type java and press enter. If you do not have java installed in your system, it will show command not found or the command is not recognized. please install latest <a href='http://www.oracle.com/technetwork/java/javase/downloads/index.html'>JRE</a> and try again. If you are not sure about the version of your JRE, please install the latest version</p></li></ul>

<p><b>2. My translated document is in the same language as the original one.</b>
<ul><li><b>Cause</b> Google supports a set of language, so as we. You may have chosen a language which google does not support.<br>
</li><li><b>Solution</b> Go to the <a href='http://translate.google.com'>google translator page</a> and try to translate some text from your document. If google supports the translation, then this software should also be able to translate the same. Now, go to Help->Preference and choose HTTP Translator. for details please refer to <a href='http://code.google.com/p/google-document-translator/wiki/UserGuide'>UserGuide</a>. Again try to translate the same file. (before translation, you must remove the already translated file by clicking on it and select it again). If this also fails, please feel free to raise a bug with proper information, logs and preferably your document for which the translation failed. We will try to answer as earliest as possible.</p></li></ul>


<p><b>3. I cannot open my translated document, it is showing corrupted.</b>
<ul><li><b>Cause</b> Your file may be too big for our software to process<br>
</li><li><b>Solution</b> This software is still in early phase of development and may contain some potential bug. Try to truncate your document in two three parts and then try again with this translation. Please report bug with proper information, logs and preferably your document for which the translation failed.</p></li></ul>

<p><b>4. My translated document contains all junk characters ??????.</b>
<ul><li><b>Cause</b> There is a problem in your file encoding or your system does not support the required font or maybe both.<br>
</li><li><b>Solution</b> Please check whether your file is UTF8 encoded. Currently we support only UTF8 for text processing. Also, always try to run the software through the batch file (Gdt.bat) which is provided in the release, unless you are an advanced user.</p></li></ul>

<p><b>5. After clicking one Translate button, nothing happens.</b>
<ul><li><b>Cause</b> There can be several reason behind this. The Translator may be unable to read the input file. The input file may be corrupted or in some unknown format. Also, the Translator may not be able to connect to internet.<br>
</li><li><b>Solution</b> If it is a microsoft office document format, check whether you are able to view the file with office software. Check your internet settings. You can also check the logs for probable cause. After checking everything if you feel the software is not operating in the way it should, please do not hesitate to raise a bug. We will handle with priority</p>