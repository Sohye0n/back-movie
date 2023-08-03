async function loginToken() {
    const response = await axios.post('http://localhost:8080/LoginForm')
    .then(response=>{
        console.log("hi")
    });
}
