function submitt() {
    let username = document.getElementById("Username").value;
    //console.log(email1)
    let password1 = document.getElementById("password").value;
    //console.log(password1)
    localStorage.setItem(username, password1);

}
document.querySelector(".login1").addEventListener("submit", submitt2);
function submitt2(e) {
    e.preventDefault();
    let username1 = document.getElementById("uname").value;
    //console.log(email1)
    let password1 = document.getElementById("pwd").value;
    //console.log(password1)
    sessionStorage.setItem(username1, password1);
    let flag = 1
    for (var i = 0; i < localStorage.length; i++) {
        // console.log(localStorage.getItem(localStorage.key(i)))
        // console.log(localStorage.key(i))

        if (username1 == localStorage.key(i) && password1 == localStorage.getItem(localStorage.key(i)) && username1 != "" && password1 != "") {
            console.log("success")
            flag = 0;
            location.href = "candidateLoginSucces.html";

        }

    }
    if (flag == 1) {
        window.alert("Invalid Credentials")

    }


}
function home() {
    location.href = "index.html";
}
