// @author Rob W <http://stackoverflow.com/users/938089/rob-w>
// Demo: var serialized_html = DOMtoString(document);

function DOMtoString(document_root) {
    var html = document.title +" "+ document_root.location.href;

    return html;
}

chrome.extension.sendMessage({
    action: "getSource",
    source: DOMtoString(document)
});
