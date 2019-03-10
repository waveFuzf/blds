<!DOCTYPE html
        PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <link rel="stylesheet" href="E:\blds\target\classes\iconfont.css" type="text/css" />
    <style>
        body {
            padding: 0;
            margin: 0;
            font-family: SimSun;
        }
        ul {
            list-style: none;
        }
        ul li{
            margin-top: 30px;
        }
        .info ul {
            list-style: none;
            float: left;
            width: 30%;
            padding-left: 0px;
        }
        .info ul li {
            margin-top: 10px;
        }
        .info ul li span{
            margin-left: 10px;
        }
        .title {
            text-align: right;
            width: 120px;
            float: left;
        }

    </style>
</head>

<body>
<div style="color:#606266;">
    <div style="margin:10px auto;text-align:center;font-size:24px;font-weight:bold;color: black;">${consultDetail.doctors[1].hospitalName}</div>
    <div style="margin:10px auto;text-align:center;font-size:16px;">远程病理诊断咨询报告单</div>
    <div style="position:relative;float:right;margin-top:-25px;margin-right:20px;width:200px">
        会诊编号：${consultDetail.consultNo}
    </div>
    <div style="border:1px solid #ebebeb;margin:10px auto;"></div>
    <div style="width:100%;height:170px;" class="info"><ul>
        <li><div class="title">患者姓名</div><span>${consultDetail.consultPatient.patientName}</span></li>
        <li><div class="title">门诊号/住院号</div><span>${consultDetail.consultPatient.mzNum}</span></li>
        <li><div class="title">申请医生</div><span>${consultDetail.doctors[0].doctorName}</span></li>
    </ul>
        <ul>
            <li><div class="title">性别</div><span>${consultDetail.consultPatient.sex}</span></li>
            <li><div class="title">原病理号</div><span>${consultDetail.consultPatient.mzNum}</span></li>
            <li><div class="title">申请科室</div><span>${consultDetail.doctors[1].doctorDepartment}</span></li>
        </ul>
        <ul style="width: 40%;">
            <li><div class="title">年龄</div><span>${consultDetail.consultPatient.age}</span></li>
            <li><div class="title">部位</div><span>${consultDetail.parts}</span></li>
            <li><div class="title">申请医院</div><span>${consultDetail.doctors[1].hospitalName}</span></li>
        </ul></div>
        显微图片
    <div style="border:1px solid #ebebeb;margin:10px auto;"></div>
    <div>
    <#list consultDetail.hzSlides as p>
        <img style="width:40%;margin:20px 4.8%;" src="${p.clientSlidePath}"></img>
    </#list>
    </div>
    会诊意见
    <div style="border:1px solid #ebebeb;margin:10px auto;"></div>

    <div>
        <ul>
            <li>镜下所见<span style="margin-left:30px;">${form.mirrorView}</span></li>
            <li>专家意见<span style="margin-left:30px;">${form.diagnose}</span></li>
            <li>会诊专家<span style="margin-left:30px;">${consultDetail.doctors[1].doctorName}</span>
                <div style="float:right;margin-right: 30px;">报告日期<span>${date}</span></div>
            </li>
        </ul>
    </div>
    <div style="margin:10px auto;width:90%;font-size:12px;">
        注：本报告仅对送检数字切片负责，此会诊结果仅供临床医生或提供送检切片单位的病理医师
        参考，如临床医生或病理医师对本报告有疑问，请及时与会诊专家联系！
    </div>
</div>
</body>

</html>
