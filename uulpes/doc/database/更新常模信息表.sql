insert into scale_norminfo (name, type, createtime, scale_id ) 
select title as name,1 as type,now() as createtime,s.code as scale_id from scale s 
join scale_norm sn on s.code=sn.scale_id group by s.code,s.title

update scale_norm n
join scale_norminfo sn on n.scale_id=sn.scale_id
set n.norm_id=sn.id