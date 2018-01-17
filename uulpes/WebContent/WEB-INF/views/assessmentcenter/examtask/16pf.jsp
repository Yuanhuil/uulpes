<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
	
<style type="text/css">
	.img1{
		width:266px;
		height:612px;
		position:relative;
		background-image: url(${baseaction}/reportchart.do?${page.image1});
		background-position:center;
	}
</style>
<c:set var="sdimlist" value="${page.sDimList}"/>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=#ffcb97 style="font-size:12px;line-height:15px;margin:auto;">
        <tbody>
        <tr bgcolor=#ffffff height="20px" >
          <td width=60 rowspan="2" align="center">人格因素</td>
          <td width=48 rowspan="2" align="center">原始分</td>
          <td width=48 rowspan="2" align="center">标准分</td>
          <td width=142 rowspan="2" align="center">低分者特性</td>
          <td width=266 align="center">标准分</td>
          <td width=122 rowspan="2" align="center">高分者特性</td>
          </tr>
        <tr bgcolor=#ffffff height="20px">
          <td align="center"><table width="100%">
          		<tr>
          			<td width="10%">1</td><td width="10%">2</td><td width="10%">3</td><td width="10%">4</td><td width="10%">5</td>
          			<td width="10%">6</td><td width="10%">7</td><td width="10%">8</td><td width="10%">9</td><td width="10%">10</td>
          		</tr>
          	</table>
          </td>
        </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">A.合群性</td>
          <td align="center">${sdimlist[0].rawScore}</td>
          <td align="center">${sdimlist[0].stdScore}</td>
          <td>缄默、孤独、冷漠</td>
          <td rowspan="16" ><div class="img1"></div></td>
          <td>外向、热情、乐群</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">B.聪慧性</td>
          <td align="center">${sdimlist[1].rawScore}</td>
          <td align="center">${sdimlist[1].stdScore}</td>
          <td>思想迟钝，学识浅薄，抽象思考能力弱</td>
          <td>聪明、富有才识，善于抽象思考</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">C.稳定性</td>
          <td align="center">${sdimlist[2].rawScore}</td>
          <td align="center">${sdimlist[2].stdScore}</td>
          <td>情绪激动，易生烦恼</td>
          <td>情绪稳定而成熟，能面对现实</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">E.恃强性</td>
          <td align="center">${sdimlist[3].rawScore}</td>
          <td align="center">${sdimlist[3].stdScore}</td>
          <td>谦逊、顺从、通融、恭顺</td>
          <td>好强固执，独立积极</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">F.兴奋性</td>
          <td align="center">${sdimlist[4].rawScore}</td>
          <td align="center">${sdimlist[4].stdScore}</td>
          <td>严肃、审慎、冷静、寡言</td>
          <td>轻松兴奋，随遇而安</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">G.有恒性</td>
          <td align="center">${sdimlist[5].rawScore}</td>
          <td align="center">${sdimlist[5].stdScore}</td>
          <td>苟且敷衍，缺乏奉公守法的精神</td>
          <td>有恒负责，做事尽职</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">H.敢为性</td>
          <td align="center">${sdimlist[6].rawScore}</td>
          <td align="center">${sdimlist[6].stdScore}</td>
          <td>畏怯退缩，缺乏自信心</td>
          <td>冒险敢为，少有顾忌</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">I.敏感性</td>
          <td align="center">${sdimlist[7].rawScore}</td>
          <td align="center">${sdimlist[7].stdScore}</td>
          <td>理智的，自重现实</td>
          <td>敏感，感情用事</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">L.怀疑性</td>
          <td align="center">${sdimlist[8].rawScore}</td>
          <td align="center">${sdimlist[8].stdScore}</td>
          <td>信赖随和，易与人相处</td>
          <td>怀疑、刚愎、固执己见</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">M.幻想性</td>
          <td align="center">${sdimlist[9].rawScore}</td>
          <td align="center">${sdimlist[9].stdScore}</td>
          <td>现实、合乎常规</td>
          <td>幻想、狂放不羁</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">N.世故性</td>
          <td align="center">${sdimlist[10].rawScore}</td>
          <td align="center">${sdimlist[10].stdScore}</td>
          <td>坦白、直率、天真</td>
          <td>精明能干、世故</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">O.忧虑性</td>
          <td align="center">${sdimlist[11].rawScore}</td>
          <td align="center">${sdimlist[11].stdScore}</td>
          <td>安详沉着、有自信心</td>
          <td>忧虑抑郁、烦恼自扰</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">Q1.实验性</td>
          <td align="center">${sdimlist[12].rawScore}</td>
          <td align="center">${sdimlist[12].stdScore}</td>
          <td>保守、尊重传统观念与行为标准</td>
          <td>自由、批评激进、不拘于现实</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">Q2.独立性</td>
          <td align="center">${sdimlist[13].rawScore}</td>
          <td align="center">${sdimlist[13].stdScore}</td>
          <td>依赖、随群附合</td>
          <td>自立自强、当机立断</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">Q3.自律性</td>
          <td align="center">${sdimlist[14].rawScore}</td>
          <td align="center">${sdimlist[14].stdScore}</td>
          <td>矛盾冲突、不明大体</td>
          <td>知已知彼、自律严谨</td>
          </tr>
        <tr bgcolor=#ffffff height="36px">
          <td align="center">Q4.紧张性</td>
          <td align="center">${sdimlist[15].rawScore}</td>
          <td align="center">${sdimlist[15].stdScore}</td>
          <td>心平气和、闲散宁静</td>
          <td>紧张困扰、激动挣扎</td>
		</tr></tbody></table>