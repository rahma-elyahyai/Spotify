let total = 0;

const quizquestions = [
    {
        question: "1. Who is the highest run-scorer for RCB in IPL history?",
        options: ["Chris Gayle", "AB de Villiers", "Virat Kohli", "Rahul Dravid"],
        answer: '3'
    },

    {
        question: "2.Which bowler holds the record for the most wickets taken for RCB in a single IPL season?",
        options: ["Harshal Patel", "Yuzvendra Chahal", "Anil Kumble", "Zaheer Khan"],
        answer: '1'
    }
    ,
    {
        question: "3.What is RCB's highest team total in an IPL match?",
        options: ["263/5", "248/3", "227/4", "211/3"],
        answer: '1'
    },

    {
        question: "4.In which year did RCB reach their first IPL final?",
        options: ["2009", "2011", "2016", "2019"],
        answer: '1'
    }
    ,
    {
        question: "5.Who was the captain of RCB during the inaugral IPL season in 2008?",
        options: ["Kevin Pieterson", "Anil Kumble", "Virat Kohli", "Rahul Dravid"],
        answer: '4'
    }
  

]


let i = 0;
function check() {

    answer()

    if (i == 5) {

        window.alert(`Your total is ${total}`);

    }
    let header = document.getElementById('question')
    header.innerText = quizquestions[i].question;



    let opt1 = document.getElementById('option1')
    let opt2 = document.getElementById('option2')
    let opt3 = document.getElementById('option3')
    let opt4 = document.getElementById('option4')

    opt1.innerText = quizquestions[i].options[0]
    opt2.innerText = quizquestions[i].options[1]
    opt3.innerText = quizquestions[i].options[2]
    opt4.innerText = quizquestions[i].options[3]


    document.querySelector('input[name="flexRadioDefault"]:checked').checked=false
   
}

function answer() {
    let ans = document.querySelector('input[name="flexRadioDefault"]:checked').value;
    if (ans == quizquestions[i].answer) {

        total++;

    }

    i++;

   
}