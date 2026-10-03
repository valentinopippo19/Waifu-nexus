const chars=[
{id:1,name:'Akari',anime:'Crimson Academy',element:'FIRE',atk:80,def:35,hp:180,img:'images/akari.png',story:'Una duelista impulsiva de Crimson Academy. Canaliza fuego en ataques explosivos y protege a sus compañeros con una determinación que nunca se apaga.'},
{id:2,name:'Mizuki',anime:'Azure Moon',element:'WATER',atk:65,def:55,hp:200,img:'images/mizuki.png',story:'Guardiana de Azure Moon. Su estilo combina paciencia y resistencia: prefiere leer al rival, absorber el golpe y responder en el momento exacto.'},
{id:3,name:'Yoru',anime:'Nightfall Academy',element:'SHADOW',atk:90,def:30,hp:170,img:'images/yoru.png',story:'Una combatiente de Nightfall Academy que se mueve entre sombras. Tiene el ataque más alto del grupo, pero debe cuidar su defensa.'},
{id:4,name:'Luna',anime:'Otaku Legends',element:'LIGHT',atk:76,def:50,hp:195,img:'images/luna.png',story:'Una viajera llegada desde Otaku Legends. Su afinidad con la luz la convierte en una luchadora equilibrada, capaz de adaptarse a distintas formaciones.'},
{id:5,name:'Fuyumi',anime:'Sky Garden',element:'WIND',atk:72,def:42,hp:190,img:'images/fuyumi.png',story:'Habitante de Sky Garden. Controla las corrientes de aire para mantener la distancia y atacar con precisión, mezclando velocidad y estabilidad.'},
{id:6,name:'Hikari',anime:'Starlight Saga',element:'LIGHT',atk:70,def:60,hp:210,img:'images/hikari.png',story:'Una guerrera de Starlight Saga especializada en resistencia. Sus altas defensas y gran reserva de vida la hacen una presencia difícil de derribar.'}
];
const $=s=>document.querySelector(s), $$=s=>[...document.querySelectorAll(s)];
let openingUrl=null,endingUrl=null,mode='opening',allies=[],enemies=[],turn=1,battleOver=false,log=[],attackerId=1,targetId=2,strategy='EQUILIBRADA';
const clone=c=>({...c,currentHp:c.hp,ko:false,damage:0});
function show(id){$$('.screen').forEach(s=>s.classList.remove('active'));$('#'+id).classList.add('active');window.scrollTo(0,0)}
function characterCards(){
  $('#characterGrid').innerHTML=chars.map(c=>`<article class="character"><img src="${c.img}" alt="${c.name}"><div class="character-body"><div class="character-top"><h3>${c.name}</h3><span class="badge">${c.element}</span></div><div class="anime">${c.anime}</div><div class="story">${c.story}</div><div class="stats"><span>ATK ${c.atk}</span><span>DEF ${c.def}</span><span>HP ${c.hp}</span></div></div></article>`).join('');
}
function optionCards(team){return chars.map(c=>`<button class="select-card" data-team="${team}" data-id="${c.id}"><img src="${c.img}" alt="${c.name}"><strong>${c.name}</strong><small>${c.element} · ATK ${c.atk} · HP ${c.hp}</small></button>`).join('')}
function renderSelection(){
  $('#allyOptions').innerHTML=optionCards('ally');$('#enemyOptions').innerHTML=optionCards('enemy');
  $$('.select-card').forEach(b=>b.onclick=()=>{const id=+b.dataset.id,team=b.dataset.team;let arr=team==='ally'?allies:enemies;const other=team==='ally'?enemies:allies;if(other.some(x=>x.id===id))return;if(arr.some(x=>x.id===id))arr=arr.filter(x=>x.id!==id);else if(arr.length<3)arr=[...arr,chars.find(c=>c.id===id)];if(team==='ally')allies=arr;else enemies=arr;renderSelectionState()});
  renderSelectionState();
}
function renderSelectionState(){
  $$('.select-card').forEach(b=>{const id=+b.dataset.id,team=b.dataset.team,arr=team==='ally'?allies:enemies;b.classList.toggle('selected',arr.some(c=>c.id===id));b.disabled=(team==='ally'?enemies:allies).some(c=>c.id===id)});
  $('#allyCount').textContent=`${allies.length}/3`;$('#enemyCount').textContent=`${enemies.length}/3`;$('#startBattle').disabled=!allies.length||!enemies.length;
}
function logAdd(s){log.push(s);$('#combatLog').innerHTML=log.map(x=>`<div>${x}</div>`).join('');$('#combatLog').scrollTop=999999}
function stratDamage(a,d,s){if(s==='AGRESIVA')return Math.max(1,a.atk+20-Math.floor(d.def/3));if(s==='DEFENSIVA')return Math.max(1,a.atk-Math.floor(d.def/2));return Math.max(1,a.atk+Math.floor(a.def/5)-Math.floor(d.def/2))}
function alive(arr){return arr.filter(x=>!x.ko&&x.currentHp>0)}
function get(arr,id){return arr.find(x=>x.id===id)}
function renderBattle(){
  $('#turnTitle').textContent=`TURNO ${turn}`;
  $('#allyCards').innerHTML=allies.map(c=>card(c)).join('');$('#enemyCards').innerHTML=enemies.map(c=>card(c)).join('');
  const aa=alive(allies),ee=alive(enemies); if(!aa.some(x=>x.id===attackerId))attackerId=aa[0]?.id;if(!ee.some(x=>x.id===targetId))targetId=ee[0]?.id;
  $('#attackerSelect').innerHTML=aa.map(c=>`<option value="${c.id}">${c.name}</option>`).join('');$('#targetSelect').innerHTML=ee.map(c=>`<option value="${c.id}">${c.name}</option>`).join('');$('#attackerSelect').value=attackerId;$('#targetSelect').value=targetId;$('#strategySelect').value=strategy;
  $('#battleStatus').textContent=battleOver?'COMBATE FINALIZADO':'Las eliminadas desaparecen del campo y no pueden volver a participar.';
}
function card(c){const pct=Math.max(0,Math.round(c.currentHp/c.hp*100));return `<div class="combat-card ${c.ko?'ko':''}"><img src="${c.img}" alt="${c.name}"><div><h3>${c.name} · ${c.element}</h3><div class="bar"><span style="width:${pct}%"></span></div><div class="hptext">HP ${c.currentHp}/${c.hp} · ${c.ko?'KO':'ACTIVA'}</div></div></div>`}
function beginBattle(){allies=allies.map(clone);enemies=enemies.map(clone);turn=1;battleOver=false;log=[];attackerId=allies[0].id;targetId=enemies[0].id;strategy='EQUILIBRADA';show('battle');logAdd(`⚔ Nuevo combate: ${allies.length} aliadas vs ${enemies.length} enemigas.`);renderBattle();save()}
function attack(){if(battleOver)return;const a=get(allies,+$('#attackerSelect').value),d=get(enemies,+$('#targetSelect').value);if(!a||!d||a.ko||d.ko)return;strategy=$('#strategySelect').value;const dmg=stratDamage(a,d,strategy);d.currentHp=Math.max(0,d.currentHp-dmg);a.damage+=dmg;logAdd(`⚔ TU TURNO — ${a.name} usa ${strategy} y causa ${dmg} de daño a ${d.name}.`);if(d.currentHp===0){d.ko=true;logAdd(`☠ ${d.name} desaparece del campo de batalla.`)}if(checkEnd())return;renderBattle();save();setTimeout(enemyAttack,500)}
function enemyAttack(){if(battleOver)return;const e=alive(enemies)[(turn-1)%Math.max(1,alive(enemies).length)],t=get(allies,attackerId)&&!get(allies,attackerId).ko?get(allies,attackerId):alive(allies)[0];if(!e||!t)return;const strategies=['AGRESIVA','EQUILIBRADA','DEFENSIVA'];const s=strategies[turn%3];const dmg=stratDamage(e,t,s);t.currentHp=Math.max(0,t.currentHp-dmg);e.damage+=dmg;logAdd(`👿 TURNO ENEMIGO — ${e.name} usa ${s} y causa ${dmg} de daño a ${t.name}.`);if(t.currentHp===0){t.ko=true;logAdd(`☠ ${t.name} desaparece del campo de batalla.`)}turn++;const na=alive(allies)[0],ne=alive(enemies)[0];if(na)attackerId=na.id;if(ne)targetId=ne.id;if(checkEnd())return;renderBattle();save()}
function heal(){if(battleOver)return;const a=get(allies,+$('#attackerSelect').value);if(!a||a.ko)return;a.currentHp=Math.min(a.hp,a.currentHp+35);logAdd(`💚 ${a.name} recupera 35 HP.`);if(checkEnd())return;renderBattle();save();setTimeout(enemyAttack,450)}
function checkEnd(){if(!alive(allies).length){finish(false);return true}if(!alive(enemies).length){finish(true);return true}return false}
function score(c){return Math.max(0,c.currentHp)+c.damage*2}
function finish(victory){battleOver=true;renderBattle();const title=victory?'🏆 VICTORIA':'💀 DERROTA';$('#resultTitle').textContent=title;$('#resultTitle').style.color=victory?'var(--green)':'var(--red)';$('#resultSubtitle').textContent=victory?'Todas las enemigas fueron derrotadas.':'Tu equipo fue derrotado.';$('#resultCards').innerHTML=allies.map(c=>`<div class="result-card"><img src="${c.img}" alt="${c.name}"><h3>${c.name}</h3><div>HP ${c.currentHp}/${c.hp}</div><div class="muted">Puntaje: ${score(c)}</div></div>`).join('');$('#resultScore').textContent=`PUNTAJE FINAL DEL EQUIPO: ${allies.reduce((n,c)=>n+score(c),0)}`;if(endingUrl)playMedia('ending');else show('result');}
function playMedia(which){mode=which;show('mediaScreen');const v=$('#mediaPlayer'),url=which==='opening'?openingUrl:endingUrl;if(!url){which==='opening'?beginBattle():show('result');return}$('#mediaEyebrow').textContent=which==='opening'?'OPENING':'ENDING';$('#mediaTitle').textContent=which==='opening'?'WAIFU NEXUS':'BATTLE COMPLETE';v.src=url;v.currentTime=0;v.onended=()=>which==='opening'?beginBattle():show('result');v.play().catch(()=>{});}
function save(){localStorage.setItem('waifuNexusWebSave',JSON.stringify({allies,enemies,turn,log,attackerId,targetId,strategy}))}
function load(){try{const s=JSON.parse(localStorage.getItem('waifuNexusWebSave'));return s&&s.allies?.length&&s.enemies?.length?s:null}catch{return null}}
$('#openingInput').onchange=e=>{if(e.target.files[0]){$('#openingName').textContent=e.target.files[0].name;openingUrl=URL.createObjectURL(e.target.files[0])}};
$('#endingInput').onchange=e=>{if(e.target.files[0]){$('#endingName').textContent=e.target.files[0].name;endingUrl=URL.createObjectURL(e.target.files[0])}};
$('#playButton').onclick=()=>show('selection');$('#continueButton').onclick=()=>{const s=load();if(!s)return;allies=s.allies;enemies=s.enemies;turn=s.turn;log=s.log||[];attackerId=s.attackerId;targetId=s.targetId;strategy=s.strategy||'EQUILIBRADA';battleOver=false;show('battle');renderBattle();};$('#startBattle').onclick=()=>openingUrl?playMedia('opening'):beginBattle();$('#skipMedia').onclick=()=>mode==='opening'?beginBattle():show('result');$('#attackButton').onclick=attack;$('#healButton').onclick=heal;$('#saveButton').onclick=()=>{save();$('#battleStatus').textContent='✓ Combate guardado en este navegador.'};$('#attackerSelect').onchange=e=>attackerId=+e.target.value;$('#targetSelect').onchange=e=>targetId=+e.target.value;$('#strategySelect').onchange=e=>strategy=e.target.value;$('#resultHome').onclick=()=>{localStorage.removeItem('waifuNexusWebSave');allies=[];enemies=[];show('landing')};$$('[data-home]').forEach(b=>b.onclick=()=>show('landing'));
characterCards();renderSelection();if(load()){$('#continueButton').hidden=false;}
