
      let nodes = null;
      let edges = null;
      let network = null;
      let data=null;
      let updatedNodes=null;
      let localPhotoURL;
      let x,y;

      function imageChange(event){
        let selectedFile=event.target.files[0];
        const reader=new FileReader();
        reader.readAsDataURL(selectedFile);
        reader.onload=function(event){
          const dataURL=event.target.result;
          console.log(dataURL);
          localPhotoURL=dataURL;
        }
        console.log(localPhotoURL);
      }

      function saveNodes() { //updatedNodes(수정된 노드) 정보를 controller로 전송
        let xhr1 = new XMLHttpRequest();
        xhr1.open('POST', '/network/board/'+parseInt(BoardID));
        xhr1.setRequestHeader('Content-Type', 'application/json');
        xhr1.send(JSON.stringify(updatedNodes));
        updatedNodes.splice(0, updatedNodes.length);
      }

      function setDefaultNodes(){ //페이지 업로드 시 노드 초기화
        nodes=[];
        updatedNodes=[];
        let element;
        console.log(nodeList.length);

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
        console.log("set default node is running...");
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
        addNode:function(data,callback){
          document.getElementById("operation").innerText="Add character";
          document.getElementById("addButton").onclick = addData.bind(this, data, callback);
          document.getElementById("cancelButton").onclick = clearPopUp.bind();
          document.getElementById("network-popup").style.display = "block";
          document.getElementById("network-popup").style.display = "block";
          document.getElementById("network-popup").style.left=`${450+x}px`;
          document.getElementById("network-popup").style.top=`${300+y}px`;
          console.log(x,y);
          document.getElementById("addButton").style.display="inline-block";
          document.getElementById("selectedButtons").style.display="none";
          document.getElementById("node-name").value=null;
          document.getElementById("node-details").value=null;
        }
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

      function addData(data, callback) { //createdNodes[],nodes[]에 노드 추가
      console.log("addData is running!\n");
        data.id=vis.util.randomUUID(); //인물관계도 내의 노드를 구별하기 위한 view 상의 id 지정
        console.log("id:"); console.log(data.id);
        data.label = document.getElementById("node-name").value;
        data.title=document.getElementById("node-details").value;
        data.shape="image";
        console.log("addData\n");
/*        console.log(document.getElementById("node-photourl").value);
        console.log(document.getElementById("node-localpath").value);*/

        if(document.getElementById("node-photourl").value){
          data.image=document.getElementById("node-photourl").value;
          updatedNodes.push({type:0,id:data.id,details:data.title,name:data.label,isHub:0,PhotoUrl:data.image});
        }
        else{
          //const imagePath=document.getElementById("node-localpath").value;
          //const lastSlashIndex = imagePath.lastIndexOf('/'); // 파일 경로에서 마지막 슬래시 위치 찾기.
          //const lastBackslashIndex = imagePath.lastIndexOf('\\'); //파일 경로에서 마지막 역슬래시 위치를 찾습니다.
          //const startIndex = Math.max(lastSlashIndex, lastBackslashIndex) + 1; // 파일 경로에서 가장 마지막 슬래시 또는 역슬래시의 위치를 기준으로 파일 이름을 추출.
          //const fileName = imagePath.slice(startIndex);

          if(document.getElementById("node-localpath").value) {
            data.image=localPhotoURL;
            updatedNodes.push({type:0,id:data.id,details:data.title,name:data.label,isHub:0,PhotoUrl:localPhotoURL});
          }
          else {
            data.shape="circularImage";
            data.image="../images/"
            updatedNodes.push({type:0,id:data.id,details:data.title,name:data.label,isHub:0,PhotoUrl:data.image});
          }
        }

        nodes.push(data);
        network.setData({nodes:nodes});
        clearPopUp();
        console.log(data.id);
        //callback(data);
      }

      function delNode(selectedNode){
        let index=nodes.findIndex(e=>e.id==selectedNode.id);
        if (index !== -1) nodes.splice(index,1);
        network.setData({nodes:nodes});
        //var edges=getConnectedEdges(delNode);
        //edges.forEach(e=>removeEdge(e));
        index = updatedNodes.findIndex(e=>e.id==selectedNode.id&&e.type==0);
        if(index!=-1) updatedNodes.splice(index,1);
        else  updatedNodes.push({type:1, id:selectedNode.id,details:selectedNode.title,name:selectedNode.label,isHub:false,PhotoUrl:null});
        clearPopUp();
      }

      function updateNode(selectedNode){
        selectedNode.label = document.getElementById("node-name").value;
        selectedNode.title=document.getElementById("node-details").value;

        let index = updatedNodes.findIndex(e=>e.id==selectedNode.id&&e.type==0);
        if (index !== -1){
          updatedNodes[index].name=selectedNode.label;
          updatedNodes[index].details=selectedNode.title;
        }
        else  updatedNodes.push({type:2, id:selectedNode.id,details:selectedNode.title,name:selectedNode.label,isHub:false,PhotoUrl:null});
        network.setData({nodes:nodes});
        clearPopUp();
      }

      function onNode(param){
        let selectedNode=nodes.find(item=>item.id===param.nodes[0]);

        document.getElementById("operation").innerText="Update character";
        document.getElementById("updateButton").onclick=updateNode.bind(this,selectedNode);
        document.getElementById("delButton").onclick = delNode.bind(this,selectedNode);
        document.getElementById("cancelButton").onclick = clearPopUp.bind();

        document.getElementById("node-name").value=selectedNode.label;
        document.getElementById("node-details").value=selectedNode.title;

        document.getElementById("network-popup").style.display = "block";
        document.getElementById("addButton").style.display="none";
        document.getElementById("selectedButtons").style.display = "inline-block";
      }

      function init(){
        setDefaultNodes();
        draw();
        network.on("selectNode", onNode);
        network.on("click", function(params){
          x = params.pointer.canvas.x;
          y = params.pointer.canvas.y;
        });
      }

      window.addEventListener("load", ()=>{init();});