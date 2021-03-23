
ajax.Event = {};
ajax.Event.addListener = function(element, name, observer, useCapture) {
useCapture = useCapture || false;
   
if (element.addEventListener) {
element.addEventListener(name, observer, useCapture);
} else if (element.attachEvent) {
element.attachEvent('on' + name, observer);
}
};
ajax.Event.removeListener = function(element, name, observer, useCapture) {
useCapture = useCapture || false;
   
if (element.removeEventListener) {
element.removeEventListener(name, observer, useCapture);
} else if (element.detachEvent) {
element.detachEvent('on' + name, observer);
}
};


ajax.Event.getTarget = function(event) {
if (event == null) return null;
if (event.target) return event.target;
else if (event.srcElement) return event.srcElement;
return null;
};