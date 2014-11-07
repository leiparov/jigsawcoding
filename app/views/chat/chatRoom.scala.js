@(username: String)

$(function() {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
 var chatSocket = new WS("@routes.ChatController.chat(username).webSocketURL(request)")

    var sendMessage = function() {
        chatSocket.send(JSON.stringify(
            {text: $("#talk").val()}
        ))
        $("#talk").val('')
    }

    var receiveEvent = function(event) {
        var data = JSON.parse(event.data)

        // Handle errors
        if(data.error) {
            chatSocket.close()
            $("#onError span").text(data.error)
            $("#onError").show()
            return
        } else {
            $("#onChat").show()
        }

        // Create the message element
        var el = $('<li><span id="imagen"><img src="" class="img-circle" /></span><div class="chat-body clearfix"><div class="header"><strong class="primary-font">Jack Sparrow</strong> <small class="pull-right text-muted"></div><p></p></div></li>')
        $("strong", el).text(data.user)
        $("p", el).text(data.message)
        // $(el).addClass(data.kind)
        if(data.kind == 'join' ){
        	$("img", el).attr("src", "http://placehold.it/50/AAAAAA/fff&text=U")
        	$("li", el).addClass('left clearfix')
        	$("#imagen", el).addClass('chat-img pull-left')
        }else{
        	if(data.user == '@username') {        
            	$("img", el).attr("src", "http://placehold.it/50/FA6F57/fff&text="+data.icon)
            	$("li", el).addClass('right clearfix')
            	$("#imagen", el).addClass('chat-img pull-right')
            }else{
            	$("img", el).attr("src", "http://placehold.it/50/55C1E7/fff&text="+data.icon)
            	$("li", el).addClass('left clearfix')
            	$("#imagen", el).addClass('chat-img pull-left')
            }
        }
        
        $('#messages').append(el)

        // Update the members list
        $("#members").html('')
        $(data.members).each(function() {
            var li = document.createElement('li');
            li.textContent = this;
            $("#members").append(li);
        })
    }

    var handleReturnKey = function(e) {
        if(e.charCode == 13 || e.keyCode == 13) {
            e.preventDefault()
            sendMessage()
        }
    }

    $("#talk").keypress(handleReturnKey)
    $("#btn-chat").click(function(){
    	sendMessage();
    })

    chatSocket.onmessage = receiveEvent

})
