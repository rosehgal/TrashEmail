
var xmlhttp = new XMLHttpRequest();
var url = "https://telegram.trashemail.in/admin/stats";
var adminStats;

xmlhttp.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200)
        adminStats = JSON.parse(this.responseText)
};

xmlhttp.open("GET", url, false);
xmlhttp.send();

document.getElementById("numberOfUsers").innerText = adminStats.numberOfUsers;
document.getElementById("numberOfEmailsRegistered").innerText = adminStats.numberOfEmailsRegistered;
document.getElementById("numberOfEmailsRegisteredToday").innerText = adminStats.emailIdsCreatedToday;
document.getElementById("version").innerText = adminStats.version;

