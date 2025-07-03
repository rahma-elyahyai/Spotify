let container = document.getElementById("add")
container.addEventListener('click', addnewblock);
let tbodyel = document.querySelector("tbody");
const numbers=new Set();
function addnewblock() {

    let collegeName1 = document.getElementById("exampleFormControlInput1").value
    let collegeCode = document.getElementById("exampleFormControlInput2").value
    let branch = document.getElementById("exampleDataList").value
    let preferenceNo = document.getElementById("exampleFormControlInput3").value
    if(collegeName1=="" || collegeCode=="" || branch=="" || preferenceNo=="" )
    {
        window.alert("enter all the fields")
        return
    }
    if(numbers.has(preferenceNo))
    {
        window.alert("Enter a different preference number")
        return
    }
    else
    {
        numbers.add(preferenceNo);
    }
    tbodyel.innerHTML += `
    <tr>
        <td>${collegeName1}</td>
        <td>${collegeCode}</td>
         <td>${branch}</td>
        <td>${preferenceNo}</td>
        <td> <div>
                    <button type="submit" id="edit" class="btn btn-primary">Edit</button>
                    <button type="submit" id="delete1" class="btn btn-danger delete1" ;">Delete</button>
                </div></td>

    </tr>

`
   
}
let delete1 = document.getElementById("del1")
delete1.addEventListener('click',deletevalue);

function deletevalue()
{
    
    let collegeName1 = document.getElementById("exampleFormControlInput1")
    let collegeCode = document.getElementById("exampleFormControlInput2")
    let branch = document.getElementById("exampleDataList")
    let preferenceNo = document.getElementById("exampleFormControlInput3")
    collegeName1.value="";
    collegeCode.value="";
    branch.value="";
    preferenceNo.value="";
}
let del=document.querySelector("tbody");
del.addEventListener("click",(e)=>{
    if(e.target.classList.contains("delete1"))
    {
        console.log("delete");
        e.target.parentElement.parentElement.parentElement.remove();
    }
 

})

function home()
{
    location.href="index.html";
}