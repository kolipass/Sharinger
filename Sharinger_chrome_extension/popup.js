chrome.extension.onMessage.addListener(function(request, sender) {
  if (request.action == "getSource") {
    message.innerText ="Copied: "+ request.source;
    copyTextToClipboard(request.source);

  }
});

function onWindowLoad() {

  var message = document.querySelector('#message');

  chrome.tabs.executeScript(null, {
    file: "getPagesTitleAndUrl.js"
  }, function() {
    // If you try and inject into an extensions page or the webstore/NTP you'll get an error
    if (chrome.extension.lastError) {
      message.innerText = 'There was an error injecting script : \n' + chrome.extension.lastError.message;
    }
  });

}

function copyTextToClipboard(text) {
	var copyFrom = document.createElement("textarea");
	copyFrom.textContent = text;
	var body = document.getElementsByTagName('body')[0];
	body.appendChild(copyFrom);
	copyFrom.select();
	document.execCommand('copy');
	body.removeChild(copyFrom);
}


window.onload = onWindowLoad;
