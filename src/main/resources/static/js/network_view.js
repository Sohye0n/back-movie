
let nodes = null;
      let edges = null;
      let network = null;
      let data=null;
      let updatedNodes=null;

      function saveNetwork() { //현재 네트워크를 img파일로 사용자 컴퓨터에 다운로드
        const $myNetwork=document.getElementById("mynetwork");
        const canvas=document.createElement("canvas");
        const context=canvas.getContext("2d");

        html2canvas($myNetwork).then(function(canvas){
            const link=document.createElement("a");
            link.href=canvas.toDataURL("network/png");
            link.download="network.png";

            link.click();
        });
      }


      function setDefaultNodes(){ //페이지 업로드 시 노드 초기화
        nodes=[];
        updatedNodes=[];
        let element;

        function myFunction(item){
            console.log("my func is running...");
            element={id:item.id, label:item.name,title:item.details}; //id, label, title 지정
            if(item.PhotoUrl){ //image 지정
              let img='./images/'+item.PhotoUrl;  //이미지가 저장된 실제 url
              element["shape"]="circularImage";
              element["image"]=img;
            }
            nodes.push(element); //nodes[]에 추가
        }

        //controller에서 전송한 노드들을 nodes[]에 저장
        nodeList.forEach(myFunction);
        /*[/]*/
        data={nodes:nodes,edges:edges};
      }

      function destroy() {
          if (network !== null) {
            network.destroy();
            network = null;
          }
      }

      function draw(){
          destroy();
          let options={
              interaction:{hover:true},
              manipulation:{
                enabled:false,
              },
          }
          let container=document.getElementById("mynetwork");
          network = new vis.Network(container, data, options);
      }

      function clearPopUp() { //network-popup 레이아웃을 지우고 초기화
        document.getElementById("addButton").onclick = null;
        document.getElementById("cancelButton").onclick = null;
        document.getElementById("delButton").onclick = null;
        document.getElementById("updateButton").onclick = null;
        document.getElementById("node-name").value=null;
        document.getElementById("node-details").value=null;
        document.getElementById("network-popup").style.display = "none";
      }


      function init(){
      console.log(nodeList.length);
      console.log(BoardID);
        setDefaultNodes();
        draw();
        network.on("click", function(params){
            const clickedNode=params.nodes[0].id;
            console.log(clickedNode.id);
            const clickedNodeData=nodes.find(node=>node.id===clickedNode);
            document.getElementById("name").textContent=clickedNodeData.title;
        });
      }

      window.addEventListener("load", ()=>{init();});
