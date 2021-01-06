import { debounce } from './debounce.js'

console.log("hello")
var wsUri = "ws://localhost:8080/echo";
let outputcatcher
let websocket
function send(obj) {
    websocket.send(JSON.stringify(obj))
}
const server = { ref: 'server', caption: 'Broadcast' };
function connect() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (evt) {
        writeOutput('<strong style="color: darkgrey">Connected to server</strong>',server);
        setEnabled(true);
        send({ type: 'INFO', params: { 'username': document.getElementById('inp-username').value } });
    };
    websocket.onmessage = function (evt) {


        const m = JSON.parse(evt.data)
        let currentTypingTimeout

        if (m.type == 'BC') {
            writeOutput(`[${m.uref.caption}]: ${m.content}`,server);
        }
        else if ( m.type == 'DM' ) {
            writeOutput(`[${m.uref.caption}]: ${m.content}`, m.uref)
        }
        else if (m.type == 'INFO') {
            const list = document.querySelector('#js-users-list');
            list.innerHTML="";

            for( const u of m.content )
            {
                 const el = document.createElement('a') 
                 el.href = '#' + u.ref
                 el.text = u.caption
                 el.addEventListener('click', ev => {
                     createOrGetUserCol(u)
                 })
                 el.classList.add('btn', 'btn-outline-primary')
                 list.append(el)
            }
            // for( const u of m.content ) {
            //     createOrGetUserCol(u)
            // }
        }
        else if (m.type ="ST") {
            clearTimeout(currentTypingTimeout)
            const stEl = document.getElementById('js-status');
            stEl.textContent = `[${m.uref.caption}] ${m.content}`
            currentTypingTimeout = setTimeout(clearText, 4000, stEl)
        }
        else {
            console.log(m)
        }

    };

    function clearText(el) {
        el.textContent = ""
    }

    websocket.onclose = function (evt) {
        writeOutput('<strong style="color: darkgrey">Disconnected!</strong>', server);
        setEnabled(false);
    };

    websocket.onerror = function (evt) {
        writeOutput('<strong style="color: red">Error: ' + evt.data + '</strong>', server);
        setEnabled(false);
    };

    window.websocket = websocket;
}

function disconnect() {
    if (typeof window.websocket !== 'undefined') {
        window.websocket.close();
    }
}


function sendStatus(tArea) {
    let uref = tArea.closest('form').dataset.uref
    if(uref)
    {
        send({ type: 'ST', content: 'typing', params:{uref} });
    }
    else
    {
    send({ type: 'ST', content: 'typing' });
    }

}
function sendMessage(form) {
    var messageToSend = form.querySelector('textarea').value;
    let uref = form.dataset.uref
    if(uref)
    {
        send({ type: 'DM', content: messageToSend, params:{uref} });
    }
    else
    {
    send({ type: 'BC', content: messageToSend });
    }
    uref = uref || server.ref
    form.querySelector('[name=messageBody]').value = '';
    writeOutput("[Me]: " + messageToSend, {ref:uref,caption:''});

    return false;
}

function createOrGetUserCol(u)
{
    const domId = 'js-dm-'+u.ref
    const el = document.getElementById(domId)
    if(!el)
    {
        const bc = document.getElementById('js-stencil-col')
        const newChat = bc.cloneNode(true)
        newChat.querySelector('.card-title span').innerText = u.caption
        newChat.querySelector('.output-view').innerHTML = ""
        if(u.ref != server.ref) {
            const formEl = newChat.querySelector('form');
            formEl.dataset.uref=u.ref
        }
        newChat.id = domId
        document.getElementById('js-chat-cards').append(newChat)
        registerSendListener(newChat)
        return newChat
    }
    return el
}



function writeOutput(message, uref) {
    const el = createOrGetUserCol(uref)
    var line = document.createElement("p");
    line.innerHTML = message;
    const out = el.querySelector('.output-view');
    out.appendChild(line);
    out.scrollTop = out.scrollHeight
}

function setEnabled(isEnabled) {
    const elements = [
        ...document.querySelectorAll('[name=sendBtn]'),
        ...document.querySelectorAll('textarea'),
    ]
    if (isEnabled) {
        elements.forEach(el => el.removeAttribute('disabled') )
    } else {
        elements.forEach(el => el.setAttribute('disabled', 'disabled') );
    }
}

document.addEventListener('DOMContentLoaded', () => {
    for (const el of document.getElementsByClassName('js-btn-connect')) {
        el.addEventListener('click', connect)
    }
    for (const el of document.getElementsByClassName('js-btn-disconnect')) {
        el.addEventListener('click', disconnect)
    }

    // const col = document.getElementById('js-broadcast-col')
    // registerSendListener(col);

    document.getElementById('js-refresh-users').addEventListener('click', ev => {
        send({ type: 'INFO', params: { cmd: 'ls' } });

    })
    document.getElementById('inp-username')?.closest('form').addEventListener('submit', ev => {
        ev.preventDefault()
        connect()
    })
})

function registerSendListener(col) {

    col.querySelector('form').addEventListener('submit', ev => {
        ev.preventDefault()
        sendMessage(ev.target)
    });
    col.querySelector('.close').addEventListener('click', ev => {
        ev.preventDefault()
        col.remove()
    })

    const ds = debounce(sendStatus,1000)
    col.querySelector('[name=messageBody]').addEventListener('input', ev => {
        ds(ev.target)
    })
}
