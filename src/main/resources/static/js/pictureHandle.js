$(function () {
    var _upFile = document.getElementById("lefile");

    _upFile.addEventListener("change", function () {
        if (_upFile.files.length === 0) {
            alert("请选择图片");
            return;
        }
        var oFile = _upFile.files[0];
        if (!new RegExp("(jpg|jpeg|png)+", "gi").test(oFile.type)) {
            alert("照片上传：文件类型必须是JPG、JPEG、PNG");
            return;
        }

        var reader = new FileReader();
        reader.onload = function (e) {
            var base64Img = e.target.result;
            //--执行resize
            var _ir = ImageResizer({
                resizeMode: "auto"
                , dataSource: base64Img
                , dataSourceType: "base64"
                , maxWidth: 300 //允许的最大宽度
                , maxHeight: 300 //允许的最大高度
                , onTmpImgGenerate: function (img) {
                }
                , success: function (resizeImgBase64, canvas) {
                    //压缩后预览
                    $("#nextview").attr("src", resizeImgBase64);
                    //赋值到隐藏域传给后台
                    $('#imgOne').val(resizeImgBase64.substr(23));
                },
                debug: true
            });
        };
        reader.readAsDataURL(oFile);
    }, false);
});