// @author Rob W <http://stackoverflow.com/users/938089/rob-w>
// Demo: var serialized_html = DOMtoString(document);

function DOMtoData(document_root) {
    var data = {
        url: document_root.location.href,
        message: document.title +" "+ document_root.location.href
    }

    return data;
}

chrome.extension.sendMessage({
    action: "getSource",
    source: DOMtoData(document)
});
