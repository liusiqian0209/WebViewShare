(function android() {
    android.addText = function (str) {
        document.getElementById("remotetext").innerHTML = str;
    };

    android.getTime = function () {
        return new Date().toString();
    };

    //export
    window.android = android;
})();