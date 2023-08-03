async function loginToken() {
  try {
    const nickname=document.getElementById('Nickname').value;
    const pw=document.getElementById('pw').value;

    const data={
        nickname: nickname,
        pw: pw
    };
    console.log(data.nickname);

    const response = await axios.post('http://localhost:8080/LoginForm',data);
    console.log("hi");
    console.log(response.data.token);
    onLoginSuccess(response);


    // 이곳에서 response를 가공하거나 필요한 작업을 수행할 수 있습니다.
  } catch (error) {
    console.error(error);
    // 에러 처리를 원한다면 추가로 작성합니다.
  }
}

const onLoginSuccess=(response)=>{
    const access_token=response.headers.authorization;
    console.log(access_token);
    axios.defaults.headers.common['Authorization']=`Bearer ${access_token}`;
}
