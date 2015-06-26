For Troubleshoot and FAQ please refer [Troubleshoot](http://code.google.com/p/google-document-translator/wiki/Troubleshoot) and [FAQ](http://code.google.com/p/google-document-translator/wiki/FAQ)

# Introduction #

We have tried to keep the GUI as simple as possible and free from too many options. Document translation using this software is pretty simple. It has three obvious steps
  1. Select the document
  1. Choose the translation language pair
  1. Start translation

Still, some operations need explanation in order to perform better translation.

### Translation language selection ###
This is the most important step in your document translation. We will support only those languages which are supported by Google. Selection of invalid language pair will end up in wrong translation.
<p>If you are sure about the language of your document, please choose the appropriate option from "From:" drop down list. Do not just put "AUTO_DETECT". If your document is mixed with two or more language, google will choose what it will encounter first and you may not get your intended result.</p>
<p>There is an option in google, "detect language", which we didn't add deliberately. If you are not sure about the languge, choose "auto"</p>

### Translator type selection ###
In the Preference dialog (Help->Preference), you can choose between "HTTP" translator and "AJAX" Translator. HTTP translator uses http post method and parse the response for translation. This is the way how you use google translator in your browser. AJAX translator uses google api for translation. This is an advanced form of service, fast and reliable. But, only problem is, google will stop this service after 31st December 2011 and you need to pay if you still want to use this. Use HTTP translator only for academic purpose. We will release a paid version of AJAX translator soon.

### Proxy selection ###
If you want to configure the proxy setting, go to Help->Preference. Currently we do not support proxy authentication.