
Global Curr173.NPCs, Curr106.NPCs, Curr096.NPCs
Const NPCtype173% = 1, NPCtypeOldMan% = 2, NPCtypeGuard% = 3, NPCtypeD% = 4
Const NPCtype372% = 6, NPCtypeApache% = 7, NPCtypeMTF% = 8, NPCtype096 = 9
Const NPCtype049% = 10, NPCtypeZombie% = 11, NPCtype5131% = 12, NPCtypeTentacle% = 13
Const NPCtype860% = 14, NPCtype939% = 15, NPCtype066% = 16

Type NPCs
	Field obj%, obj2%, obj3%, obj4%, Collider%
	Field NPCtype%, ID%
	Field DropSpeed#, Gravity%
	Field State#, State2#, State3#, PrevState%
	
	Field Angle#
	Field Sound%, SoundChn%, SoundTimer#
	Field Sound2%, SoundChn2%
	
	Field Speed#, CurrSpeed#
	
	Field texture$
	
	Field Idle#
	
	Field Reload#
	
	Field LastSeen%, LastDist#
	
	Field PrevX#, PrevY#, PrevZ#
	
	Field Target.NPCs, TargetID%
	Field EnemyX#, EnemyY#, EnemyZ#
	
	Field Path.WayPoints[20], PathStatus%, PathTimer#, PathLocation%
End Type

Function CreateNPC.NPCs(NPCtype%, x#, y#, z#)
	Local n.NPCs = New NPCs, n2.NPCs
	Local temp#, i%, diff1, bump1, spec1
	Local bodybump, facebump, helmetbump
	Local sf, b, t1
	
	n\NPCtype = NPCtype
	Select NPCtype
		Case NPCtype173
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.32
			EntityType n\Collider, HIT_PLAYER
			n\Gravity = True
			
			n\obj = LoadMesh_Strict("GFX\npcs\173_2.b3d")
			temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "scale") / MeshDepth(n\obj))			
			ScaleEntity n\obj, temp,temp,temp
			
			If BumpEnabled Then 		
				diff1 = LoadTexture_Strict("GFX\npcs\173texture.png")
				bump1 = LoadTexture_Strict("GFX\npcs\173_norm.jpg")
				spec1 = LoadTexture_Strict("GFX\npcs\173_spec.jpg")
				TextureBlend bump1, FE_BUMP
				TextureBlend spec1, FE_SPECULAR0
				
				EntityTexture n\obj, spec1, 0, 0
				EntityTexture n\obj, bump1, 0, 1
				EntityTexture n\obj, diff1, 0, 2
				FreeTexture diff1
				FreeTexture bump1
				FreeTexture spec1
			EndIf
			
			;SetAnimTime n\obj, 68	
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "speed") / 100.0)
			;n\obj = LoadMesh_Strict("GFX\mesh\173.3ds")
			;scaleentity n\obj, 0.35 / meshWidth(n\obj), 0.30 / meshWidth(n\obj), 0.30 / meshWidth(n\obj)
			
			n\obj2 = LoadMesh_Strict("GFX\173box.b3d")
			ScaleEntity n\obj2, RoomScale, RoomScale, RoomScale
			HideEntity n\obj2
			
		Case NPCtypeOldMan
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			EntityType n\Collider, HIT_PLAYER
			n\obj = LoadAnimMesh_Strict("GFX\npcs\106_2.b3d")
			;n\State = 20;0000
			
			If BumpEnabled And 0 Then 		
				diff1 = LoadTexture_Strict("GFX\npcs\106_diffuse.png")
				bump1 = LoadTexture_Strict("GFX\npcs\106_normals.png")
				TextureBlend bump1, FE_BUMP
				;TextureBlend di1, FE_SPECULAR0
				
				EntityTexture n\obj, bump1, 0, 0
				EntityTexture n\obj, diff1, 0, 1
				FreeTexture diff1
				FreeTexture bump1
			EndIf
			
			;EntityShininess(n\obj, 0.6)
			
			temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "scale") / 2.2)		
			ScaleEntity n\obj, temp, temp, temp
			
			Local OldManEyes% = LoadTexture_Strict("GFX\npcs\oldmaneyes.jpg")
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "speed") / 100.0)
			
			n\obj2 = CreateSprite()
			ScaleSprite(n\obj2, 0.03, 0.03)
			EntityTexture(n\obj2, OldManEyes)
			EntityBlend (n\obj2, BLEND_ADD)
			EntityFX(n\obj2, 1 + 8)
			SpriteViewMode(n\obj2, 2)
			
			FreeTexture OldManEyes%
		Case NPCtypeGuard
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			;EntityRadius Collider, 0.15, 0.30
			EntityType n\Collider, HIT_PLAYER
			n\obj = CopyEntity(GuardObj) ;LoadAnimMesh_Strict("GFX\npcs\mtf.b3d")
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "Guard", "speed") / 100.0)
			temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
			
			ScaleEntity n\obj, temp, temp, temp
		Case NPCtypeMTF
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			;EntityRadius Collider, 0.15, 0.30
			EntityType n\Collider, HIT_PLAYER
			;EntityPickMode n\Collider, 1
			n\obj = CopyEntity(MTFObj) ;LoadAnimMesh_Strict("GFX\npcs\mtf.b3d")
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "MTF", "speed") / 100.0)
			
			temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
			
			ScaleEntity n\obj, temp, temp, temp
			
			If MTFSFX(0)=0 Then
				MTFSFX(0)=LoadSound_Strict("SFX\MTF\Stop0.ogg")
				MTFSFX(1)=LoadSound_Strict("SFX\MTF\Stop1.ogg")
				MTFSFX(2)=LoadSound_Strict("SFX\MTF\Stop2.ogg")			
				MTFSFX(3)=LoadSound_Strict("SFX\MTF\ClassD0.ogg")
				MTFSFX(4)=LoadSound_Strict("SFX\MTF\ClassD1.ogg")
				MTFSFX(5)=LoadSound_Strict("SFX\MTF\Beep.ogg")
				MTFSFX(6)=LoadSound_Strict("SFX\MTF\Breath.ogg")
			EndIf
			If MTFrooms[6]=Null Then 
				For r.Rooms = Each Rooms
					Select Lower(r\RoomTemplate\Name)
						Case "room106"
							MTFrooms[0]=r
						Case "roompj"
							MTFrooms[1]=r	
						Case "room079"
							MTFrooms[2]=r	
						Case "room2poffices"
							MTFrooms[3]=r	
						Case "914"
							MTFrooms[4]=r	
						Case "coffin"
							MTFrooms[5]=r	
						Case "start"
							MTFrooms[6]=r		
					End Select
				Next			
			EndIf
		Case NPCtypeD
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.32
			EntityType n\Collider, HIT_PLAYER
			
			n\obj = CopyEntity(ClassDObj)
			
			MeshCullBox (n\obj, -MeshWidth(ClassDObj), -MeshHeight(ClassDObj), -MeshDepth(ClassDObj), MeshWidth(ClassDObj)*2, MeshHeight(ClassDObj)*2, MeshDepth(ClassDObj)*2)
			
			temp# = 0.5 / MeshWidth(n\obj)
			ScaleEntity n\obj, temp, temp, temp
		Case NPCtype372
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			n\obj = LoadAnimMesh_Strict("GFX\npcs\372.b3d")
			
			temp# = 0.35 / MeshWidth(n\obj)
			ScaleEntity n\obj, temp, temp, temp
		Case NPCtype5131
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			n\obj = LoadAnimMesh_Strict("GFX\npcs\bll.b3d")
			
			n\obj2 = CopyEntity (n\obj)
			EntityAlpha n\obj2, 0.6
			
			temp# = 1.8 / MeshWidth(n\obj)
			ScaleEntity n\obj, temp, temp, temp
			ScaleEntity n\obj2, temp, temp, temp
		Case NPCtype096
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.25
			EntityType n\Collider, HIT_PLAYER
			n\obj = LoadAnimMesh_Strict("GFX\npcs\scp096.b3d")
			
			MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "speed") / 100.0)
			
			temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "scale") / 3.0)
			ScaleEntity n\obj, temp, temp, temp	
		Case NPCtype049
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			EntityType n\Collider, HIT_PLAYER
			n\obj = LoadAnimMesh_Strict("GFX\npcs\scp-049.b3d")
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049", "speed") / 100.0)
			
			temp# = GetINIFloat("DATA\NPCs.ini", "SCP-049", "scale")
			ScaleEntity n\obj, temp, temp, temp	
			
			n\Sound = LoadSound_Strict("SFX\Horror12.ogg")
		Case NPCtypeZombie
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			EntityType n\Collider, HIT_PLAYER
			
			For n2.NPCs = Each NPCs
				If n\NPCtype = n2\NPCtype And n<>n2 Then
					n\obj = CopyEntity (n2\obj)
					Exit
				EndIf
			Next
			
			If n\obj = 0 Then 
				n\obj = LoadAnimMesh_Strict("GFX\npcs\zombie1.b3d")
				
				MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
				
				temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "scale") / 2.5)
				ScaleEntity n\obj, temp, temp, temp
			EndIf
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "speed") / 100.0)
			
			SetAnimTime(n\obj, 107)
			
			n\Sound = LoadSound_Strict("SFX\zombiebreath.ogg")
		Case NPCtypeApache
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			n\obj = CopyEntity(ApacheObj);LoadAnimMesh_Strict("GFX\apache.b3d")
			
			n\obj2 = CopyEntity(ApacheRotorObj);LoadAnimMesh_Strict("GFX\apacherotor.b3d",n\obj)
			EntityParent n\obj2,n\obj
			
			For i = -1 To 1 Step 2
				Local rotor2 = CopyEntity(n\obj2,n\obj2)
				RotateEntity rotor2,0,4.0*i,0
				EntityAlpha rotor2, 0.5
			Next
			
			n\obj3 = LoadAnimMesh_Strict("GFX\apacherotor2.b3d",n\obj)
			PositionEntity n\obj3, 0.0, 2.15, -5.48
			
			EntityType n\Collider, HIT_APACHE
			EntityRadius n\Collider, 3.0
			
			For i = -1 To 1 Step 2
				Local Light1 = CreateLight(2,n\obj)
				;room\LightDist[i] = range
				LightRange(Light1,2.0)
				LightColor(Light1,255,255,255)
				PositionEntity(Light1, 1.65*i, 1.17, -0.25)
				
				Local lightsprite = CreateSprite(n\obj)
				PositionEntity(lightsprite, 1.65*i, 1.17, 0, -0.25)
				ScaleSprite(lightsprite, 0.13, 0.13)
				EntityTexture(lightsprite, LightSpriteTex(0))
				EntityBlend (lightsprite, 3)
				EntityFX lightsprite, 1+8				
			Next
			
			temp# = 0.6
			ScaleEntity n\obj, temp, temp, temp
		Case NPCtypeTentacle
			n\Collider = CreatePivot()
			
			For n2.NPCs = Each NPCs
				If n\NPCtype = n2\NPCtype And n<>n2 Then
					n\obj = CopyEntity (n2\obj)
					Exit
				EndIf
			Next
			
			If n\obj = 0 Then 
				n\obj = LoadAnimMesh_Strict("GFX\NPCs\035tentacle.b3d")
				ScaleEntity n\obj, 0.065,0.065,0.065
			EndIf
			
			SetAnimTime n\obj, 283
		Case NPCtype860
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.25
			EntityType n\Collider, HIT_PLAYER
			n\obj = LoadAnimMesh_Strict("GFX\npcs\forestmonster.b3d")
			
			EntityFX(n\obj, 1)
			
			tex = LoadTexture_strict("GFX\npcs\860_eyes.png",1+2)
			
			n\obj2 = CreateSprite()
			ScaleSprite(n\obj2, 0.1, 0.1)
			EntityTexture(n\obj2, tex)
			FreeTexture tex
			
			EntityFX(n\obj2, 1 + 8)
			EntityBlend(n\obj2, BLEND_ADD)
			SpriteViewMode(n\obj2, 2)
			
			MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "forestmonster", "speed") / 100.0)
			
			temp# = (GetINIFloat("DATA\NPCs.ini", "forestmonster", "scale") / 20.0)
			ScaleEntity n\obj, temp, temp, temp	
		Case NPCtype939
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.3
			EntityType n\Collider, HIT_PLAYER
			For n2.NPCs = Each NPCs
				If n\NPCtype = n2\NPCtype And n<>n2 Then
					n\obj = CopyEntity (n2\obj)
					Exit
				EndIf
			Next
			
			If n\obj = 0 Then 
				n\obj = LoadAnimMesh_Strict("GFX\NPCs\scp-939.b3d")
				
				If BumpEnabled Then
					bump1 = LoadTexture_Strict("GFX\npcs\scp-939_licker_normal.png")
					TextureBlend bump1, FE_BUMP
					
					For i = 1 To CountSurfaces(n\obj)
						sf = GetSurface(n\obj,i)
						b = GetSurfaceBrush( sf )
						t1 = GetBrushTexture(b,0)
						
						Select Lower(StripPath(TextureName(t1)))
							Case "scp-939-licker_diffusetest01.png"
								
								BrushTexture b, bump1, 0, 0
								BrushTexture b, t1, 0, 1
								PaintSurface sf,b
								
								If StripPath(TextureName(t1)) <> "" Then FreeTexture t1
								FreeBrush b	
						End Select
					Next
					FreeTexture bump1
				EndIf
				
				temp# = GetINIFloat("DATA\NPCs.ini", "SCP-939", "scale")/2.5
				ScaleEntity n\obj, temp, temp, temp		
			EndIf
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 100.0)
		Case NPCtype066
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			EntityType n\Collider, HIT_PLAYER
			
			n\obj = LoadAnimMesh_Strict("GFX\NPCs\scp-066.b3d")
			temp# = GetINIFloat("DATA\NPCs.ini", "SCP-066", "scale")/2.5
			ScaleEntity n\obj, temp, temp, temp		
			
			If BumpEnabled Then 
				diff1 = LoadTexture_Strict("GFX\npcs\scp-066_diffuse01.png")
				bump1 = LoadTexture_Strict("GFX\npcs\scp-066_normal.png")
				TextureBlend bump1, FE_BUMP
				TextureBlend spec1, FE_SPECULAR0
				EntityTexture n\obj, bump1, 0, 1
				EntityTexture n\obj, diff1, 0, 2
				FreeTexture diff1
				FreeTexture bump1
			EndIf
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-066", "speed") / 100.0)
	End Select
	
	PositionEntity(n\Collider, x, y, z, True)
	PositionEntity(n\obj, x, y, z, True)
	
	ResetEntity(n\Collider)
	
	temp = 1
	For n2.NPCs = Each NPCs
		If n2\ID > 0 Then
			temp=temp+1
		EndIf
	Next
	
	n\ID = temp
	
	Return n
End Function

Function RemoveNPC(n.NPCs)
	
	If n=Null Then Return
	
	If n\obj2 <> 0 Then 
		FreeEntity n\obj2
		n\obj2 = 0
	EndIf
	If n\obj3 <> 0 Then 
		FreeEntity n\obj3
		n\obj3 = 0
	EndIf
	If n\obj4 <> 0 Then 
		FreeEntity n\obj4
		n\obj4 = 0
	EndIf
	
	If n\Sound<>0 Then FreeSound n\Sound
	If n\Sound2<>0 Then FreeSound n\Sound2
	
	FreeEntity(n\obj) : n\obj = 0
	FreeEntity(n\Collider) : n\Collider = 0	
	
	Delete n
End Function


Function UpdateNPCs()
	Local n.NPCs, n2.NPCs, d.Doors, de.Decals, r.Rooms
	Local i%, dist#, dist2#, angle#, x#, y#, z#, prevframe#
	
	Local target
	
	For n.NPCs = Each NPCs
		Select n\NPCtype
			Case NPCtype173
				;[Block]
				If Disabled173 Then
					HideEntity n\obj
					HideEntity n\Collider
				Else
					
					
					dist# = EntityDistance(n\Collider, Collider)		
					
					If n\Idle < 2 Then 
						PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
						RotateEntity (n\obj, 0, EntityYaw(n\Collider)-180, 0)
						
						If n\Idle = False Then
							Local temp% = False
							Local move% = True
							If dist < 15 Then
								If dist < 10.0 Then 
									If EntityVisible(n\Collider, Collider) Then
										temp = True
										n\EnemyX = EntityX(Collider, True)
										n\EnemyY = EntityY(Collider, True)
										n\EnemyZ = EntityZ(Collider, True)
									EndIf
								EndIf										
								
								Local SoundVol# = Max(Min((Distance(EntityX(n\Collider), EntityZ(n\Collider), n\PrevX, n\PrevZ) * 2.5), 1.0), 0.0)
								n\SoundChn = LoopSound2(StoneDragSFX, n\SoundChn, Camera, n\Collider, 10.0, n\State)
								
								n\PrevX = EntityX(n\Collider)
								n\PrevZ = EntityZ(n\Collider)				
								
								If (BlinkTimer < - 16 Or BlinkTimer > - 6) Then
									If EntityInView(n\obj, Camera) Then move = False
								EndIf
							EndIf
							
							;player is looking at it -> doesn't move
							If move=False Then
								;If temp Then
									BlurVolume = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), BlurVolume)
									CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs())/20.0)+1.0)*15.0*Max((3.5-dist)/3.5,0.0))								
									
									If dist < 3.5 And MilliSecs() - n\LastSeen > 60000 Then 
										If Rand(2)=1 Then
											PlaySound(HorrorSFX(3))
										Else
											PlaySound(HorrorSFX(4))
										EndIf
										
										n\LastSeen = MilliSecs()
									EndIf
									
									If dist < 1.5 And Rand(700) = 1 Then PlaySound2(Scp173SFX(Rand(0, 2)), Camera, n\obj)
									
									If dist < 1.5 And n\LastDist > 2.0 Then
										CurrCameraZoom = 40.0
										HeartBeatRate = Max(HeartBeatRate, 140)
										HeartBeatVolume = 0.5
										
										Select Rand(5)
											Case 1
												PlaySound(HorrorSFX(1))
											Case 2
												PlaySound(HorrorSFX(2))
											Case 3
												PlaySound(HorrorSFX(9))
											Case 4
												PlaySound(HorrorSFX(10))
											Case 5
												PlaySound(HorrorSFX(14))
										End Select
									EndIf									
								;EndIf
								n\LastDist = dist
								
								n\State = Max(0, n\State - FPSfactor / 20)
							Else 
								;more than 6 room lengths away from the player -> teleport to a room closer to the player
								If dist > 50 Then
									If Rand(70)=1 Then
										If PlayerRoom\RoomTemplate\Name <> "exit1" And PlayerRoom\RoomTemplate\Name <> "gatea" And PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
											For w.waypoints = Each WayPoints
												If w\door=Null Then
													If Rand(5)=1 Then
														x = Abs(EntityX(Collider)-EntityX(w\obj,True))
														If x < 25.0 And x > 15.0 Then
															z = Abs(EntityZ(Collider)-EntityZ(w\obj,True))
															If z < 25 And z > 15.0 Then
																DebugLog "MOVING 173 TO "+w\room\roomtemplate\name
																PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
																ResetEntity n\Collider
																Exit
															EndIf
														EndIf
													EndIf
												EndIf
											Next
										EndIf
									EndIf
								ElseIf dist > HideDistance*0.8 ;3-6 rooms away from the player -> move randomly from waypoint to another
									If Rand(70)=1 Then
										For w.WayPoints = Each WayPoints
											If w\door = Null Then
												xtemp = Abs(EntityX(w\obj,True)-EntityX(n\Collider,True))
												If xtemp < 6.0 And xtemp > 1.0 Then 
													ztemp = Abs(EntityZ(w\obj,True)-EntityZ(n\Collider,True))
													If ztemp < 6.0 And ztemp > 1.0 Then 
														ytemp = Abs(EntityY(w\obj,True)-EntityY(n\Collider,True))
														If ytemp < 6.0 Then 
															PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.15, EntityZ(w\obj,True), True
															ResetEntity n\Collider
														EndIf
													EndIf
												EndIf
											EndIf
										Next
									EndIf
								Else ;less than 3 rooms away -> actively move towards the player
									n\State = CurveValue(SoundVol, n\State, 3)
									If Rand(15) = 1 Then
										For d.Doors = Each Doors
											If (Not d\locked) And d\open = False And d\Code = "" And d\KeyCard=0 Then
												For i% = 0 To 1
													If d\buttons[i] <> 0 Then
														If Abs(EntityX(n\Collider) - EntityX(d\buttons[i])) < 0.5 Then
															If Abs(EntityZ(n\Collider) - EntityZ(d\buttons[i])) < 0.5 Then
																If (d\openstate >= 180 Or d\openstate <= 0) Then
																	pvt = CreatePivot()
																	PositionEntity pvt, EntityX(n\Collider), EntityY(n\Collider) + 0.5, EntityZ(n\Collider)
																	PointEntity pvt, d\buttons[i]
																	MoveEntity pvt, 0, 0, n\Speed * 0.6
																	
																	If EntityPick(pvt, 0.5) = d\buttons[i] Then 
																		PlaySound (LoadTempSound("SFX\Doors\DoorOpen173.ogg"))
																		UseDoor(d,False)
																	EndIf
																	
																	FreeEntity pvt
																EndIf
															EndIf
														EndIf
													EndIf
												Next
											EndIf
										Next
									EndIf
									
									;player is not looking and is visible from 173's position -> attack
									If temp Then 				
										If dist < 0.65 Then
											If KillTimer >= 0 And (Not GodMode) Then
												
												Select PlayerRoom\RoomTemplate\Name
													Case "lockroom", "room2closets", "coffin"
														DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. The surveillance tapes confirm that the subject was killed by SCP-173."	
													Case "173"
														If Rand(2)=1 Then
															DeathMSG = "''Yes, we we're just about to start the tests when it started. The Class Ds and the guard died as soon as the lights started flickering, "
															DeathMSG = DeathMSG + "but I managed to get out of the surveillance room before it got me.''"
														Else
															DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. According to Security Chief Franklin who was present at SCP-173's containment "
															DeathMSG = DeathMSG + "chamber during the breach, the subject was killed by SCP-173 as soon as the disruptions in the electrical network started."
														EndIf
													Case "room2doors"
														DeathMSG = "''If I'm not mistaken, one of the main purposes of these rooms was to stop SCP-173 from moving further in the event of a containment breach. "
														DeathMSG = DeathMSG + "So, who's brilliant idea was it to put A GODDAMN MAN-SIZED VENTILATION DUCT in it?''"
													Default 
														DeathMSG = "Subject D-9341. Cause of death: Fatal cervical fracture. Assumed to be attacked by SCP-173."	
												End Select
												
												If (Not GodMode) Then n\Idle = True
												PlaySound(DamageSFX(Rand(0, 2)))
												If Rand(2) = 1 Then 
													TurnEntity(Camera, 0, Rand(80,100), 0)
												Else
													TurnEntity(Camera, 0, Rand(-100,-80), 0)
												EndIf
												Kill()
												
											EndIf
										Else
											PointEntity(n\Collider, Collider)
											RotateEntity n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider)
											MoveEntity(n\Collider, 0, 0, n\Speed * FPSfactor)
										EndIf
										
									Else ;player is not visible -> move to the location where he was last seen							
										If n\EnemyX <> 0 Then						
											If Distance(EntityX(n\Collider), EntityZ(n\Collider), n\EnemyX, n\EnemyZ) > 0.5 Then
												AlignToVector(n\Collider, n\EnemyX-EntityX(n\Collider), 0, n\EnemyZ-EntityZ(n\Collider), 3)
												MoveEntity(n\Collider, 0, 0, n\Speed * FPSfactor)
												If Rand(500) = 1 Then n\EnemyX = 0 : n\EnemyY = 0 : n\EnemyZ = 0
											Else
												n\EnemyX = 0 : n\EnemyY = 0 : n\EnemyZ = 0
											End If
										Else
											If Rand(400)=1 Then RotateEntity (n\Collider, 0, Rnd(360), 10)
											
											MoveEntity(n\Collider, 0, 0, n\Speed * 0.8 * FPSfactor)
											;TurnEntity (n\Collider, 0, 8.0*FPSfactor, 10)
										End If
									EndIf
									
								EndIf ; less than 2 rooms away from the player
								
							EndIf
							
						EndIf ;idle = false
						
						PositionEntity(n\Collider, EntityX(n\Collider), Min(EntityY(n\Collider),0.35), EntityZ(n\Collider))
						
					Else ;idle = 2
						
						If n\Target <> Null Then
							If dist < HideDistance<0.7 Then
								PointEntity n\obj, n\Target\Collider
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0, True								
								dist = EntityDistance(n\Collider, n\Target\Collider)
								MoveEntity n\Collider, 0, 0, 0.008*FPSfactor*Max(Min((dist*2-1.0)*0.5,1.0),-0.5) 								
							Else
								PointEntity n\Collider, n\Target\Collider
								RotateEntity n\Collider, 0, n\Collider, 0, True			
								dist = EntityDistance(n\Collider, n\Target\Collider)
								MoveEntity n\Collider, 0, 0, dist-0.6
							EndIf
							
							
							For r.Rooms = Each Rooms
								If r\RoomTemplate\Name = "start" Then
									If Distance(EntityX(n\Collider),EntityZ(n\Collider),EntityX(r\obj,True)+1024*RoomScale,EntityZ(r\obj,True)+384*RoomScale)<1.6 Then
										n\Idle = 3
										n\Target = Null
									EndIf
									Exit
								EndIf
							Next
						EndIf
						
						PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) + 0.05 + Sin(MilliSecs()*0.08)*0.02, EntityZ(n\Collider))
						RotateEntity (n\obj, 0, EntityYaw(n\Collider)-180, 0)
						
						ShowEntity n\obj2
						
						PositionEntity(n\obj2, EntityX(n\Collider), EntityY(n\Collider) - 0.05 + Sin(MilliSecs()*0.08)*0.02, EntityZ(n\Collider))
						RotateEntity (n\obj2, 0, EntityYaw(n\Collider)-180, 0)
					EndIf
					
				EndIf
				
				;[End block]
			Case NPCtypeOldMan ;------------------------------------------------------------------------------------------------------------------
				;[Block]
				If Contained106 Then
					n\Idle = True
					HideEntity n\obj
					HideEntity n\obj2
					PositionEntity n\obj, 0,500.0,0, True
				Else
					
					dist = EntityDistance(n\Collider, Collider)
					
					If (Not n\Idle) Then
						If n\State <= 0 Then	;attacking	
							If EntityY(n\Collider) < EntityY(Collider) - 20.0 - 0.55 Then
								If Not PlayerRoom\RoomTemplate\DisableDecals Then
									de.Decals = CreateDecal(0, EntityX(Collider), 0.01, EntityZ(Collider), 90, Rand(360), 0)
									de\Size = 0.05 : de\SizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals
								EndIf
								PositionEntity(n\Collider, EntityX(Collider), EntityY(Collider) - 15, EntityZ(Collider))
								PlaySound(DecaySFX(0))
							End If
							
							If Rand(500) = 1 Then PlaySound2(OldManSFX(Rand(0, 2)), Camera, n\Collider)
							n\SoundChn = LoopSound2(OldManSFX(4), n\SoundChn, Camera, n\Collider, 8.0, 0.8)
							
							If n\State > - 10 Then
								ShouldPlay = 66
								If AnimTime(n\obj)<259 Then
									
									PositionEntity n\Collider, EntityX(n\Collider), EntityY(Collider) - 0.35, EntityZ(n\Collider)
									PointEntity n\obj, Collider
									RotateEntity (n\Collider, 0, CurveValue(EntityYaw(n\obj),EntityYaw(n\Collider),100.0), 0, True)
									
									Animate2(n\obj, AnimTime(n\obj), 110, 259, 0.15, False)
									
								Else
									n\State = -10
								EndIf
							Else
								If PlayerRoom\RoomTemplate\Name <> "gatea" Then ShouldPlay = 10
								
								Local Visible% = EntityVisible(n\Collider, Collider)
								
								If EntityInView(n\Collider, Camera) Then
									Achievements(Achv106) = True
									
									BlurVolume = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), BlurVolume)
									CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs())/20.0)+1.0) * 20.0 * Max((4.0-dist)/4.0,0))
									
									If MilliSecs() - n\LastSeen > 60000 Then 
										CurrCameraZoom = 40
										PlaySound(HorrorSFX(6))
										n\LastSeen = MilliSecs()
									EndIf
								Else
									
									If (Not Visible) Then
										n\State=n\State-FPSfactor
									EndIf
								End If
								
								If dist > 0.8 Then
									If (dist > 40.0 Or n\PathStatus=2 Or (n\PathStatus=0 And n\PathTimer>0) Or PlayerRoom\RoomTemplate\Name = "pocketdimension" Or Visible) And PlayerRoom\RoomTemplate\Name <> "gatea" Then 
										
										TranslateEntity n\Collider, 0, ((EntityY(Collider) - 0.14) - EntityY(n\Collider)) / 50.0, 0
										
										PointEntity n\obj, Collider
										;rotateentity(n\collider, entityPitch(n\collider),))
										RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
										
										If KillTimer >= 0 Then
											n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
											Local atemp# = AnimTime(n\obj)
											Animate2(n\obj, AnimTime(n\obj), 284, 333, n\CurrSpeed*40)
											
											If atemp =< 286 And AnimTime(n\obj)>286 Then
												PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))	
											ElseIf atemp=<311 And AnimTime(n\obj)>311.0 
												PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))
											EndIf
										Else 
											n\CurrSpeed = 0
										EndIf
										
										n\PathTimer = Max(n\PathTimer-FPSfactor,0)
										If n\PathTimer =< 0 Then n\PathStatus = 0
									Else 
										
										If n\PathTimer <= 0 Then
											n\PathStatus = FindPath (n, EntityX(Collider,True), EntityY(Collider,True), EntityZ(Collider,True))
											n\PathTimer = 70*10
											n\CurrSpeed = 0
										Else
											n\PathTimer = Max(n\PathTimer-FPSfactor,0)
											
											If n\PathStatus = 2 Then
												n\CurrSpeed = 0
											ElseIf n\PathStatus = 1
												If n\Path[n\PathLocation]=Null Then 
													If n\PathLocation > 19 Then 
														n\PathLocation = 0 : n\PathStatus = 0
													Else
														n\PathLocation = n\PathLocation + 1
													EndIf
												Else
													TranslateEntity n\Collider, 0, ((EntityY(n\Path[n\PathLocation]\obj,True) - 0.15) - EntityY(n\Collider)) / 50.0, 0
													
													PointEntity n\obj, n\Path[n\PathLocation]\obj
													
													RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
													n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
													
													atemp# = AnimTime(n\obj)
													Animate2(n\obj, AnimTime(n\obj), 284, 333, n\CurrSpeed*40)
													If atemp =< 286 And AnimTime(n\obj)>286 Then
														PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))	
													ElseIf atemp=<311 And AnimTime(n\obj)>311.0 
														PlaySound2(Step2SFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))
													EndIf
													
													If EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) < 0.2 Then
														n\PathLocation = n\PathLocation + 1
													EndIf 
												EndIf
												;rotateentity(n\collider, entityPitch(n\collider),))
											ElseIf n\PathStatus = 0
												If n\State3=0 Then Animate2(n\obj, AnimTime(n\obj), 334, 494, 0.3)
												n\CurrSpeed = CurveValue(0,n\CurrSpeed,10.0)
											EndIf
										EndIf
										
									EndIf
									
								ElseIf PlayerRoom\RoomTemplate\Name <> "gatea" ;dist < 0.8
									
									If dist > 0.5 Then 
										n\CurrSpeed = CurveValue(n\Speed * 2.5,n\CurrSpeed,10.0)
									Else
										n\CurrSpeed = 0
									EndIf
									Animate2(n\obj, AnimTime(n\obj), 105, 110, 0.15, False)
									;If Floor(AnimTime(n\obj)) = 43 Then SetAnimTime(n\obj, 43)
									
									If KillTimer >= 0 And FallTimer >= 0 Then
										PointEntity n\obj, Collider
										RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0										
										
										If Ceil(AnimTime(n\obj)) = 110 Then
											PlaySound(DeathSFX(1))
											PlaySound(HorrorSFX(5))											
											If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
												DeathMSG = "Subject D-9341. Body partially decomposed by what is assumed to be SCP-106's ''corrosion'' effect. Body disposed of via incineration."
												Kill()
											Else
												PlaySound(OldManSFX(3))
												FallTimer = Min(-1, FallTimer)
												PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
												ResetEntity (Head)
												RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
											EndIf
										EndIf
									EndIf
									
								EndIf
								
							EndIf 
							
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
							
							n\DropSpeed = 0
							If n\State <= Rand(-2800, -2200) Then 
								If Not EntityInView(n\obj,Camera) Then n\State = Rand(22000, 27000)
							EndIf
							
							If FallTimer < -250.0 Then
								For r.Rooms = Each Rooms
									If r\RoomTemplate\Name = "pocketdimension" Then
										FallTimer = 0
										UpdateDoors()
										UpdateRooms()
										ShowEntity Collider
										PlaySound(Use914SFX)
										PlaySound(OldManSFX(5))
										PositionEntity(Collider, EntityX(r\obj),0.8,EntityZ(r\obj))
										DropSpeed = 0
										ResetEntity Collider
										n\State = -2500
										BlinkTimer = -10
										Injuries = Injuries+0.5
										
										Exit
									EndIf
								Next												
							EndIf
							
						Else ;idling outside the map
							n\CurrSpeed = 0
							MoveEntity n\Collider, 0, ((EntityY(Collider) - 30) - EntityY(n\Collider)) / 200.0, 0
							n\DropSpeed = 0
							SetAnimTime n\obj, 110.0
							
							If (Not PlayerRoom\RoomTemplate\DisableDecals) Then n\State=n\State-FPSfactor
						End If
						
						ResetEntity(n\Collider)
						PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.15, EntityZ(n\Collider))
						
						RotateEntity n\obj, 0, EntityYaw(n\Collider), 0
						
						PositionEntity(n\obj2, EntityX(n\obj), EntityY(n\obj) , EntityZ(n\obj))
						RotateEntity(n\obj2, 0, EntityYaw(n\Collider) - 180, 0)
						MoveEntity(n\obj2, 0, 8.6 * 0.11, -1.5 * 0.11)
						
						If PlayerRoom\RoomTemplate\Name = "pocketdimension" Or PlayerRoom\RoomTemplate\Name = "gatea" Then
							HideEntity n\obj2
						Else
							If dist < CameraFogFar*LightVolume*0.6 Then
								HideEntity n\obj2
							Else
								ShowEntity n\obj2
								EntityAlpha (n\obj2, Min(dist-CameraFogFar*LightVolume*0.6,1.0))
							EndIf
						EndIf						
					Else
						HideEntity n\obj2
					EndIf
					
				EndIf
				
				;[End Block]
			Case NPCtype096
				;[Block]
				dist = EntityDistance(Collider, n\Collider)
				
				Select n\State
					Case 0
						If dist<8.0 Then
							Achievements(Achv096) = True
							If n\Sound = 0 Then
								n\Sound = LoadSound_Strict("SFX\096_1.ogg")
							Else
								n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 8.0, 1.0)
							EndIf
							
							
							
							Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.1) ;sitting
							
							
							angle = WrapAngle(DeltaYaw(n\Collider, Collider));-EntityYaw(n\Collider,True))
							;DebugLog angle+": "+DeltaYaw(n\Collider, Collider)+" - "+EntityYaw(n\Collider,True)
							
							If angle<90 Or angle>360-90 Then
								
								CameraProject Camera,EntityX(n\Collider), EntityY(n\Collider)+0.25, EntityZ(n\Collider)
								
								DebugLog ProjectedX()+", "+ProjectedY()
								
								If ProjectedX()>0 And ProjectedX()<GraphicWidth Then
									If ProjectedY()>0 And ProjectedY()<GraphicHeight Then
										If EntityVisible(Collider, n\Collider) Then
											PlaySound LoadTempSound("SFX\096_5.ogg")
											
											CurrCameraZoom = 10
											
											SetAnimTime n\obj, 307
											StopChannel n\SoundChn
											FreeSound n\Sound
											n\Sound = 0
											n\State = 1
										EndIf									
									EndIf
								EndIf								
								
							EndIf
							
							;FreeEntity pvt
						EndIf
					Case 4
						CurrCameraZoom = CurveValue(Max(CurrCameraZoom, (Sin(Float(MilliSecs())/20.0)+1.0) * 10.0),CurrCameraZoom,8.0)
						
						DebugLog n\Speed+" - "+n\CurrSpeed
						
						If n\Target = Null Then 
							If n\Sound = 0 Then
								n\Sound = LoadSound_Strict("SFX\096_4.ogg")
							Else
								n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 7.5, 1.0)
							EndIf
							
							If n\Sound2 = 0 Then
								n\Sound2 = LoadSound_Strict("SFX\096_3.ogg")
							Else
								If n\SoundChn2 = 0 Then
									n\SoundChn2 = PlaySound (n\Sound2)
								Else
									If (Not ChannelPlaying(n\SoundChn2)) Then n\SoundChn2 = PlaySound(n\Sound2)
									ChannelVolume(n\SoundChn2, Min(Max(8.0-dist,0.6),1.0))
								EndIf
							EndIf
						EndIf
						
						If KillTimer =>0 Then
							
							If MilliSecs() > n\State3 Then
								n\LastSeen=0
								If n\Target=Null Then
									If EntityVisible(Collider, n\Collider) Then n\LastSeen=1
								Else
									If EntityVisible(n\Target\Collider, n\Collider) Then n\LastSeen=1
								EndIf
								n\State3=MilliSecs()+3000
							EndIf
							
							If n\LastSeen=1 Then
								n\PathTimer=Max(70*3, n\PathTimer)
								n\PathStatus=0
								
								If n\Target<> Null Then dist = EntityDistance(n\Target\Collider, n\Collider)
								
								If dist < 2.8 Or AnimTime(n\obj)<150 Then 
									If AnimTime(n\obj)>193 Then SetAnimTime n\obj, 2 ;go to the start of the jump animation
									
									Animate2(n\obj, AnimTime(n\obj), 2, 193, 0.7)
									
									If dist > 1.0 Then 
										n\CurrSpeed = CurveValue(n\Speed*2.0,n\CurrSpeed,15.0)
										DebugLog "a"
									Else
										n\CurrSpeed = 0
										
										If n\Target=Null Then
											If (Not GodMode) Then 
												PlaySound DeathSFX(4)
												
												pvt = CreatePivot()
												CameraShake = 30
												BlurTimer = 2000
												DeathMSG = "A large amount of blood found in [DATA REDACTED]. DNA indentified as Subject D-9341. Most likely [DATA REDACTED] by SCP-096."
												Kill()
												KillAnim = 1
												For i = 0 To 6
													PositionEntity pvt, EntityX(Collider)+Rnd(-0.1,0.1),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.1,0.1)
													TurnEntity pvt, 90, 0, 0
													EntityPick(pvt,0.3)
													
													de.Decals = CreateDecal(Rand(15,16), PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
													de\Size = Rnd(0.2,0.6) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
												Next
												FreeEntity pvt
											EndIf
										EndIf				
									EndIf
									
									If n\Target=Null Then
										PointEntity n\Collider, Collider
									Else
										PointEntity n\Collider, n\Target\Collider
									EndIf
									
								Else
									If n\Target=Null Then 
										PointEntity n\obj, Collider
									Else
										PointEntity n\obj, n\Target\Collider
									EndIf
									
									RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 5.0), 0
									
									If AnimTime(n\obj)>1000 Then n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,20.0)
									
									If AnimTime(n\obj)<1058 Then
										Animate2(n\obj, AnimTime(n\obj),892,1058, n\Speed*5, False)	
									Else
										Animate2(n\obj, AnimTime(n\obj),1059,1074, n\CurrSpeed*5)	
									EndIf
								EndIf
								
								
								RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True
								MoveEntity n\Collider, 0,0,n\CurrSpeed
								
								;If KillTimer < 0 Then
								;	n\State=2
								;	n\State2=0
								;EndIf
							Else
								If n\PathStatus = 1 Then
									
									If n\Path[n\PathLocation]=Null Then 
										If n\PathLocation > 19 Then 
											n\PathLocation = 0 : n\PathStatus = 0
										Else
											n\PathLocation = n\PathLocation + 1
										EndIf
									Else
										PointEntity n\obj, n\Path[n\PathLocation]\obj
										
										RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 5.0), 0
										
										If AnimTime(n\obj)>1000 Then n\CurrSpeed = CurveValue(n\Speed*1.5,n\CurrSpeed,15.0) : DebugLog "b"
										MoveEntity n\Collider, 0,0,n\CurrSpeed
										
										If AnimTime(n\obj)<1058 Then
											Animate2(n\obj, AnimTime(n\obj),892,1058, n\Speed*8, False)
										Else
											Animate2(n\obj, AnimTime(n\obj),1059,1084, n\CurrSpeed*8)	
										EndIf
										
										;atemp# = AnimTime(n\obj)
										;Animate2(n\obj, AnimTime(n\obj), 284, 333, n\currspeed*35)
										;If atemp > 13 And AnimTime(n\obj)<1.0 Then
														;PlaySound2(StepPDSFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))	
										;ElseIf atemp=< 7 And AnimTime(n\obj)>7.0 
														;PlaySound2(StepPDSFX(Rand(0,2)),Camera, n\Collider, 6.0, Rnd(0.8,1.0))
										;EndIf
										
										dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
										If dist2 < 0.4 Then
											If n\Path[n\PathLocation]\door <> Null Then
												If n\Path[n\PathLocation]\door\open = False Then
													n\Path[n\PathLocation]\door\open = True
													n\Path[n\PathLocation]\door\fastopen = 1
													PlaySound2(OpenDoorFastSFX, Camera, n\Path[n\PathLocation]\door\obj)
												EndIf
											EndIf							
											If dist2 < 0.2 Then n\PathLocation = n\PathLocation + 1
										EndIf 
									EndIf
									
								Else
									Animate2(n\obj, AnimTime(n\obj),892,972, 0.2)
									
									n\PathTimer = Max(0, n\PathTimer-FPSfactor)
									If n\PathTimer=<0 Then
										If n\Target<>Null Then
											n\PathStatus = FindPath(n, EntityX(n\Target\Collider),EntityY(n\Target\Collider)+0.2,EntityZ(n\Target\Collider))	
										Else
											n\PathStatus = FindPath(n, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))	
										EndIf
										n\PathTimer = 70*5
									EndIf
								EndIf
							EndIf
							
							If dist > 24.0 Or EntityY(n\Collider)<-50 Then
								If Rand(50)=1 Then
									For w.waypoints = Each WayPoints
										If w\door=Null And Rand(5)=1 Then
											x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
											If x < 28.0 And x > 20.0 Then
												z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
												If z < 28 And z > 20.0 Then
													DebugLog "TELEPORTING 096 - "+w\room\roomtemplate\name
													n\PathStatus = 0 : n\PathTimer = 0 : n\PathLocation = 0
													PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
													ResetEntity n\Collider
													Exit
												EndIf
											EndIf
										EndIf
									Next
								EndIf
							EndIf
						Else ;play the eating animation if killtimer < 0 
							Animate2(n\obj, AnimTime(n\obj), Min(27,AnimTime(n\obj)), 193, 0.5)
						EndIf
						
						
					Case 1,2,3
						If n\Sound = 0 Then
							n\Sound = LoadSound_Strict("SFX\096_2.ogg")
						Else
							n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 10.0, 1.0)
						EndIf
						
						If n\State=1 Then ; get up
							If AnimTime(n\obj)>1085 Then
								Animate2(n\obj, AnimTime(n\obj),1085,1412, 0.3,False)
								If AnimTime(n\obj)=1412 Then SetAnimTime(n\obj, 307)
							Else
								Animate2(n\obj, AnimTime(n\obj),307,424, 0.3, False)
								If AnimTime(n\obj)=424 Then n\State = 2 : SetAnimTime(n\obj, 892)								
							EndIf
						ElseIf n\State=2
							Animate2(n\obj, AnimTime(n\obj),833,972, 0.3, False)
							If AnimTime(n\obj)=>972 Then n\State = 3 : n\State2=0
						ElseIf n\State=3
							n\State2 = n\State2+FPSfactor
							If n\State2 > 70*18 Then 
								Animate2(n\obj, AnimTime(n\obj),973,1001, 0.5, False)
								If AnimTime(n\obj)>1000 Then 
									n\State = 4
									StopChannel n\SoundChn
									FreeSound n\Sound : n\Sound = 0
								EndIf
							Else
								Animate2(n\obj, AnimTime(n\obj),892,978, 0.3)
							EndIf
						EndIf
					Case 5
						If dist < 8.0 Then 
							
							n\State2=n\State2+FPSfactor
							If n\State2>1000 Then ;walking around
								If n\State2>1600 Then n\State2=Rand(0,500) : SetAnimTime(n\obj, 1652)
								
								If AnimTime(n\obj)<1652 Then ;idle to walk
									n\CurrSpeed = CurveValue(n\Speed*0.1,n\CurrSpeed,5.0)
									Animate2(n\obj, AnimTime(n\obj),1638,1652, n\CurrSpeed*45,False)
								Else
									n\CurrSpeed = CurveValue(n\Speed*0.1,n\CurrSpeed,5.0)
									Animate2(n\obj, AnimTime(n\obj),1653,1724, n\CurrSpeed*45) ;walk
								EndIf
								
								If MilliSecs() > n\State3 Then
									n\LastSeen=0
									If EntityVisible(Collider, n\Collider) Then 
										n\LastSeen=1
									Else
										HideEntity n\Collider
										EntityPick(n\Collider, 1.5)
										If PickedEntity() <> 0 Then
											n\Angle = EntityYaw(n\Collider)+Rnd(80,110)
										EndIf
										ShowEntity n\Collider
									EndIf
									n\State3=MilliSecs()+3000
								EndIf
								
								If n\LastSeen Then 
									PointEntity n\obj, Collider
									RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),130.0),0
									If dist < 1.5 Then n\State2=0
								Else
									RotateEntity n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),130.0),0
								EndIf
							Else
								If AnimTime(n\obj)>1638 Then ;walk to idle
									n\CurrSpeed = CurveValue(n\Speed*0.05,n\CurrSpeed,8.0)	
									Animate2(n\obj, AnimTime(n\obj),1652,1638, -n\CurrSpeed*45,False)
									
								Else ;idle
									n\CurrSpeed = CurveValue(0,n\CurrSpeed,4.0)		
									Animate2(n\obj, AnimTime(n\obj),585,633, 0.2) ;idle
								EndIf
							EndIf
							
							;pvt = CreatePivot()
							;PositionEntity pvt, EntityX(n\Collider),EntityY(n\Collider),EntityZ(n\Collider),True
							;PointEntity pvt, Camera
							;!!!!!!!!!!!!!!!
							angle = WrapAngle(DeltaYaw(n\Collider, Camera));-EntityYaw(n\Collider))
							If angle<55 Or angle>360-55 Then
								CameraProject Camera,EntityX(n\Collider), EntityY(Collider)+5.8*0.2-0.25, EntityZ(n\Collider)
								
								If ProjectedX()>0 And ProjectedX()<GraphicWidth Then
									If ProjectedY()>0 And ProjectedY()<GraphicHeight Then
										If EntityVisible(Collider, n\Collider) Then
											PlaySound LoadTempSound("SFX\096_5.ogg")
											
											CurrCameraZoom = 10
											
											SetAnimTime n\obj, 833
											StopChannel n\SoundChn
											FreeSound n\Sound
											n\Sound = 0
											n\State = 2
										EndIf									
									EndIf
								EndIf
								
							EndIf
								
							;angle = WrapAngle(DeltaYaw(n\Collider, Camera)-EntityYaw(n\Collider))
							;If angle<55 Or angle>360-55 Then
								;If EntityPitch(Camera)<35 And EntityPitch(Camera)>-60 Then 
									;If EntityInView(n\obj, Camera) Then 
										;If EntityVisible(Collider, n\Collider) Then
											;If n\State2<1000 Then 
											;SetAnimTime n\obj, 833
											;StopChannel n\SoundChn
											;FreeSound n\Sound
											;n\Sound = 0
											;n\State = 2
										;EndIf
									;EndIf
								;EndIf
							;EndIf
							
							;FreeEntity pvt
							
							MoveEntity n\Collider, 0,0,n\CurrSpeed
						EndIf
				End Select
				
				;ResetEntity(n\Collider)
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.02, EntityZ(n\Collider))
				
				RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider), 0
				;[End Block]
			Case NPCtype049
				;[Block]
				;n\state = the "main state" of the NPC
				;n\state2 = attacks the player when the value is above 0.0
				;n\state3 = a timer used for checking whether the player is visible every three seconds
				
				prevframe# = AnimTime(n\obj)
				
				dist  = EntityDistance(Collider, n\Collider)
				
				Select n\State
					Case 0
					Case 1
						If AnimTime(n\obj)=>538 Then
							Animate2(n\obj, AnimTime(n\obj), 659, 538, -0.45, False)
							If AnimTime(n\obj)=538 Then SetAnimTime(n\obj, 37)
						Else
							Animate2(n\obj, AnimTime(n\obj), 37, 269, 0.7, False)
							If AnimTime(n\obj)=269 Then n\State = 2
						EndIf
					Case 2 ;following the player
						
						If dist < 21.0 Then 
							
							n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider)
							
							If n\State3 < 0 Then ;check if the player is visible every three seconds
								If dist < 5.0 Then 
									If EntityVisible(Collider, n\Collider) Then n\State2 = 70*4
								EndIf
								n\State3=70*3
							Else
								n\State3=n\State3-FPSfactor
							EndIf
							
							If n\State2 > 0 Then ;player is visible -> attack
								
								n\PathStatus = 0
								
								PointEntity n\obj, Collider
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 15.0), 0
								
								If dist < 0.5 Then
									If Wearing714 Then
										BlurTimer = BlurTimer+FPSfactor*2.5
										If BlurTimer>250 And BlurTimer-FPSfactor*2.5 <= 250 And n\PrevState=0 Then
											n\SoundChn = PlaySound(LoadTempSound("SFX\049\049_8.ogg"))
											n\PrevState=1
										ElseIf BlurTimer => 500
											Wearing714=False
										EndIf
									Else
										CurrCameraZoom = 20.0
										BlurTimer = 500.0
									;Kill()
									;KillAnim = 0
										If (Not GodMode) Then 
											DeathMSG = "Three (3) active instances of SCP-049-2 discovered in the tunnel outside SCP-049's containment chamber. Terminated by Nine Tailed Fox."
											For e.events = Each Events
												If e\eventname = "room049" Then e\eventstate=-1 : Exit
											Next
											PlaySound HorrorSFX(13)
											n\State = 3
										EndIf										
									EndIf
									
								Else
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor								
									
									If dist < 3.0 Then
										Animate2(n\obj, AnimTime(n\obj), Max(Min(AnimTime(n\obj),428.0),387), 463.0, n\CurrSpeed*38)
										DebugLog "dist < 3"
									Else
										If AnimTime(n\obj)>428.0 Then
											DebugLog "AnimTime(n\obj)>428.0"
											Animate2(n\obj, AnimTime(n\obj), Min(AnimTime(n\obj),463.0), 498.0, n\CurrSpeed*38,False)
											If AnimTime(n\obj)=498 Then SetAnimTime n\obj, 358.0
										Else
											DebugLog "else"
											Animate2(n\obj, AnimTime(n\obj), Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
										EndIf
										
									EndIf
								EndIf
								
								n\State2=n\State2-FPSfactor
							Else
								n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 6.0, 0.6)
								
								If n\PathStatus = 1 Then ;path found
									If n\Path[n\PathLocation]=Null Then 
										If n\PathLocation > 19 Then 
											n\PathLocation = 0 : n\PathStatus = 0
										Else
											n\PathLocation = n\PathLocation + 1
										EndIf
									Else
										PointEntity n\obj, n\Path[n\PathLocation]\obj
										
										RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 15.0), 0
										n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
										
										;closes doors behind him
										If n\PathLocation>0 Then
											If n\Path[n\PathLocation-1]\door <> Null Then
												If n\Path[n\PathLocation-1]\door\open Then UseDoor(n\Path[n\PathLocation-1]\door, False)
											EndIf
										EndIf
										
										;opens doors in front of him
										dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj)
										If dist2 < 0.6 Then
											temp = True
											If n\Path[n\PathLocation]\door <> Null Then
												If n\Path[n\PathLocation]\door\locked Or n\Path[n\PathLocation]\door\KeyCard>0 Or n\Path[n\PathLocation]\door\Code<>"" Then
													temp = False
													n\CurrSpeed = 0
												Else
													If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
												EndIf
											EndIf
											If dist2 < 0.2 And temp Then n\PathLocation = n\PathLocation + 1
										EndIf	
										
										MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
										Animate2(n\obj, AnimTime(n\obj), Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)							
										DebugLog "path"
									EndIf
									
								Else ;no path to the player, stands still
									DebugLog "standstill"
									n\CurrSpeed = 0
									Animate2(n\obj, AnimTime(n\obj), 269, 345, 0.2)
									
									n\PathTimer = n\PathTimer-FPSfactor
									If n\PathTimer =< 0 Then
										n\PathStatus = FindPath(n, EntityX(Collider),EntityY(Collider)+0.1,EntityZ(Collider))
										n\PathTimer = n\PathTimer+70*5
									EndIf
								EndIf
							EndIf
							
							If n\CurrSpeed > 0.005 Then
								If (prevframe < 361 And AnimTime(n\obj)=>361) Or (prevframe < 377 And AnimTime(n\obj)=>377) Then
									PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))						
								ElseIf (prevframe < 431 And AnimTime(n\obj)=>431) Or (prevframe < 447 And AnimTime(n\obj)=>447)
									PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))
								EndIf
							EndIf											
						Else ;more than 3 rooms away from the player -> randomly teleport from waypoint to another
							If Rand(100)=1 Then
								If PlayerRoom\RoomTemplate\Name <> "exit1" And PlayerRoom\RoomTemplate\Name <> "gatea" And PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
									For w.waypoints = Each WayPoints
										If w\door=Null Then
											If Rand(5)=1 Then
												x = Abs(EntityX(n\Collider)-EntityX(w\obj,True))
												If x < 12.0 And x > 4.0 Then
													z = Abs(EntityZ(n\Collider)-EntityZ(w\obj,True))
													If z < 12 And z > 4.0 Then
														DebugLog "MOVING 049 TO "+w\room\roomtemplate\name
														PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True)
														ResetEntity n\Collider
														Exit
													EndIf
												EndIf
											EndIf
										EndIf
									Next
								EndIf
							EndIf
						EndIf
						
					Case 3
						Animate2(n\obj, AnimTime(n\obj), 537, 660, 0.7, False)
						PositionEntity n\Collider, CurveValue(EntityX(Collider),EntityX(n\Collider),20.0),EntityY(n\Collider),CurveValue(EntityZ(Collider),EntityZ(n\Collider),20.0)
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(Collider)-180.0,EntityYaw(n\Collider),40), 0
					Case 4
						If dist < 8.0 Then
							Animate2(n\obj, AnimTime(n\obj), 18, 19, 0.05)
							PointEntity n\obj, Collider	
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 45.0), 0
						ElseIf dist > 32.0
							n\State = 2
						EndIf
				End Select
				
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.22, EntityZ(n\Collider))
				
				RotateEntity n\obj, 0, EntityYaw(n\Collider), 0
				DebugLog AnimTime(n\obj)
					
				;[End Block]
			Case NPCtypeZombie
				;[Block]
				
				If Abs(EntityY(Collider)-EntityY(n\Collider))<4.0 Then
					
					prevframe# = AnimTime(n\obj)
					
					Select n\State
						Case 0
							Animate2(n\obj, AnimTime(n\obj), 719, 777, 0.2, False)
							If AnimTime(n\obj)=777 Then
								If Rand(700)=1 Then 							
									If EntityDistance(Collider, n\Collider)<5.0 Then
										SetAnimTime (n\obj, 719)	
										If Rand(3)=1 Then 
											n\State=1
											SetAnimTime n\obj, 155
										EndIf
									EndIf
								EndIf
							EndIf
						Case 1 ;stands up
							If AnimTime(n\obj)=>682 Then 
								Animate2(n\obj, AnimTime(n\obj), 926, 935, 0.3, False)
								If AnimTime(n\obj)=935 Then n\State = 2
							Else
								Animate2(n\obj, AnimTime(n\obj), 155, 682, 1.5, False)
							EndIf
						Case 2 ;following the player
							If n\State3 < 0 Then ;check if the player is visible every three seconds
								If EntityDistance(Collider, n\Collider)<5.0 Then 
									If EntityVisible(Collider, n\Collider) Then n\State2 = 70*5
								EndIf
								n\State3=70*3
							Else
								n\State3=n\State3-FPSfactor
							EndIf						
							
							If n\State2 > 0 Then ;player is visible -> attack
								n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 6.0, 0.6)
								
								n\PathStatus = 0
								
								dist = EntityDistance(Collider, n\Collider)
								
								PointEntity n\obj, Collider
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
								
								If dist < 0.7 Then 
									n\State = 3
									If Rand(2)=1 Then
										SetAnimTime n\obj, 2
									Else
										SetAnimTime n\obj, 66
									EndIf
								Else
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
									
									Animate2(n\obj, AnimTime(n\obj), 936, 1017, n\CurrSpeed*60)
									
								;If dist < 2.0 Then
								;	Animate2(n\obj, AnimTime(n\obj), Min(AnimTime(n\obj),95.0), 124.0, n\CurrSpeed*30)
								;Else
								;	Animate2(n\obj, AnimTime(n\obj), 64.0, 93.0, n\CurrSpeed*30)
								;EndIf
								EndIf
								
								n\State2=n\State2-FPSfactor
							Else
								If n\PathStatus = 1 Then ;path found
									If n\Path[n\PathLocation]=Null Then 
										If n\PathLocation > 19 Then 
											n\PathLocation = 0 : n\PathStatus = 0
										Else
											n\PathLocation = n\PathLocation + 1
										EndIf
									Else
										PointEntity n\obj, n\Path[n\PathLocation]\obj
										
										RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
										n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
										MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
										Animate2(n\obj, AnimTime(n\obj), 936, 1017, n\CurrSpeed*60)
										
										If EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) < 0.2 Then
											n\PathLocation = n\PathLocation + 1
										EndIf 
									EndIf
								Else ;no path to the player, stands still
									n\CurrSpeed = 0
									Animate2(n\obj, AnimTime(n\obj), 778, 926, 0.1)
									
									n\PathTimer = n\PathTimer-FPSfactor
									If n\PathTimer =< 0 Then
										n\PathStatus = FindPath(n, EntityX(Collider),EntityY(Collider)+0.1,EntityZ(Collider))
										n\PathTimer = n\PathTimer+70*5
									EndIf
								EndIf
							EndIf
							
						;65, 80, 93, 109, 123
							If n\CurrSpeed > 0.005 Then
								If (prevframe < 977 And AnimTime(n\obj)=>977) Or (prevframe > 1010 And AnimTime(n\obj)<940) Then
									PlaySound2(StepSFX(0,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))						
									
								EndIf
							EndIf						
						Case 3
							If AnimTime(n\obj) < 66 Then
								Animate2(n\obj, AnimTime(n\obj), 2, 65, 0.7, False)
								If prevframe < 23 And AnimTime(n\obj)=>23 Then 
									PlaySound DeathSFX(2)
									Injuries = Injuries+Rnd(0.4,1.0)
									DeathMSG = "Subject D-9341. Cause of death: multiple lacerations and severe blunt force trauma caused by an instance of SCP-049-2."
								ElseIf AnimTime(n\obj)=65 Then
									n\State = 2
								EndIf							
							Else
								Animate2(n\obj, AnimTime(n\obj), 66, 132, 0.7, False)
								If prevframe < 90 And AnimTime(n\obj)=>90 Then 
									PlaySound DeathSFX(3)
									Injuries = Injuries+Rnd(0.4,1.0)
									DeathMSG = "Subject D-9341. Cause of death: multiple lacerations and severe blunt force trauma caused by an instance of SCP-049-2."
								ElseIf AnimTime(n\obj)=132 Then
									n\State = 2
								EndIf		
							EndIf
							
					End Select
					
					PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
					
					RotateEntity n\obj, -90, EntityYaw(n\Collider), 0
				EndIf
				
				;[End Block]
			Case NPCtypeGuard ;------------------------------------------------------------------------------------------------------------------
				;[Block]
				prevframe# = AnimTime(n\obj)
				
				Select n\State
					Case 1 ;aims and shoots at the player
						Animate2(n\obj, AnimTime(n\obj), 1539, 1553, 0.2, False)
						
						If KillTimer => 0 Then
							dist = EntityDistance(n\Collider,Collider)
							
							If dist<11.0 Then
								pvt% = CreatePivot()
								PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
								PointEntity(pvt, Collider)
								RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(pvt), 0)
								
								RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
								
								PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
								PointEntity(pvt, Collider)
								RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
								
								If n\Reload = 0 And AnimTime(n\obj)>1439 Then 
									EntityPick(pvt, dist)
									If PickedEntity() = Collider Then
										
										DeathMSG = ""
										
										PlaySound2(GunshotSFX, Camera, n\Collider, 35)
										
										RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
										PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										MoveEntity (pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
										
										PointEntity pvt, Collider
										Shoot(EntityX(pvt),EntityY(pvt),EntityZ(pvt),0.9, False)
										n\Reload = 10
									Else
										n\CurrSpeed = n\Speed
									End If
								EndIf
								
								FreeEntity(pvt)									
							EndIf
							
						Else
							n\State = 0
						EndIf
					Case 2 ;shoots
						Animate2(n\obj, AnimTime(n\obj), 1539, 1553, 0.35, False)
						If n\Reload = 0 And AnimTime(n\obj) > 1545 Then 
							PlaySound2(GunshotSFX, Camera, n\Collider, 20)
							p.Particles = CreateParticle(EntityX(n\obj, True), EntityY(n\obj, True), EntityZ(n\obj, True), 1, 0.2, 0.0, 5)
							PositionEntity(p\pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							RotateEntity(p\pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
							MoveEntity (p\pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
							n\Reload = 5
						End If
					Case 3 ;follows a path
						If n\PathStatus = 2 Then
							n\State = 0
							n\CurrSpeed = 0
						ElseIf n\PathStatus = 1
							If n\Path[n\PathLocation]=Null Then 
								If n\PathLocation > 19 Then 
									n\PathLocation = 0 : n\PathStatus = 0
								Else
									n\PathLocation = n\PathLocation + 1
								EndIf
							Else
								PointEntity n\obj, n\Path[n\PathLocation]\obj
								
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
								
								Animate2(n\obj, AnimTime(n\obj), 1554, 1612, n\CurrSpeed*50)
								n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
								
								MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
								
								If EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) < 0.2 Then
									n\PathLocation = n\PathLocation + 1
								EndIf 
							EndIf
						Else
							n\CurrSpeed = 0
							n\State = 4
						EndIf
					Case 4
						Animate2(n\obj, AnimTime(n\obj), 923, 1354, 0.2)
						
						If Rand(400) = 1 Then n\Angle = Rnd(-180, 180)
						
						RotateEntity(n\Collider, 0, CurveAngle(n\Angle + Sin(MilliSecs() / 50) * 2, EntityYaw(n\Collider), 150.0), 0, True)
						
						dist# = EntityDistance(n\Collider, Collider)
						If dist < 15.0 Then
							
							If WrapAngle(EntityYaw(n\Collider)-DeltaYaw(n\Collider, Collider))<90 Then
								If EntityVisible(pvt,Collider) Then n\State = 1
							EndIf
							
						EndIf
						
					Case 5 ;following a target
						
						RotateEntity n\Collider, 0, CurveAngle(VectorYaw(n\EnemyX-EntityX(n\Collider), 0, n\EnemyZ-EntityZ(n\Collider))+n\Angle, EntityYaw(n\Collider), 20.0), 0
						
						dist# = Distance(EntityX(n\Collider),EntityZ(n\Collider),n\EnemyX,n\EnemyZ)
						
						Animate2(n\obj, AnimTime(n\obj), 1554, 1612, n\CurrSpeed*50)
						
						If dist > 2.0 Or dist < 1.0  Then
							n\CurrSpeed = CurveValue(n\Speed*Sgn(dist-1.5)*0.75, n\CurrSpeed, 10.0)
						Else
							n\CurrSpeed = CurveValue(0, n\CurrSpeed, 10.0)
						EndIf
						
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					Case 7
						
						Animate2(n\obj, AnimTime(n\obj), 923, 1354, 0.2)
						
					Case 8
					Default
						If Rand(400) = 1 Then n\PrevState = Rnd(-30, 30)
						n\PathStatus = 0
						Animate2(n\obj, AnimTime(n\obj), 923, 1354, 0.2)
						
						RotateEntity(n\Collider, 0, CurveAngle(n\Angle + n\PrevState + Sin(MilliSecs() / 50) * 2, EntityYaw(n\Collider), 50), 0, True)
				End Select
				
				If n\CurrSpeed > 0.01 Then
					If prevframe > 1610 And AnimTime(n\obj)<1595 Then
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
					ElseIf prevframe < 1587 And AnimTime(n\obj)=>1587
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
					EndIf
				EndIf
				
				
				n\Reload = Max(0, n\Reload - FPSfactor)
				;RotateEntity(n\Collider, 0, EntityYaw(n\Collider), 0, True)
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
				
				RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), 0
				;[End Block]
			Case NPCtypeMTF ;------------------------------------------------------------------------------------------------------------------
				;[Block]
				dist# = EntityDistance(Collider, n\Collider)
				
				;n\state = the "main state" of the NPC
				;n\state2 = a timer for the visibility checks between the npc and the player
				
				pvt = CreatePivot()
				
				prevframe# = AnimTime(n\obj)
				
				If n\Idle > 0 Then n\Idle = Max(0,n\Idle-FPSfactor)
				
				Select n\State
					Case 2 ;tries to kill the player
						If KillTimer < 0 Then n\State = 0
						
						If n\State2 =< 0 Then
							n\LastDist = 0
							n\State2 = 10
							
							If dist < (8.0-CrouchState) Then
								
								x# = EntityX(Collider)-EntityX(n\Collider)
								y# = EntityY(Collider)-(EntityY(n\Collider)+0.6)
								z# = EntityZ(Collider)-EntityZ(n\Collider)
								
								LinePick(EntityX(n\Collider), EntityY(n\Collider) + 0.6, EntityZ(n\Collider),x,y,z, EntityPick(pvt,8.0-CrouchState))
								
								If PickedEntity() = Collider Then 
									If n\LastSeen > 0 And n\LastSeen < 70*15 Then
										If n\Sound <> 0 Then FreeSound n\Sound : n\Sound = 0
										n\Sound = LoadSound_Strict("SFX\MTF\ThereHeIs"+Rand(1,3)+".ogg")
										PlayMTFSound(n\Sound, n)
									EndIf
									
									temp = True
									n\LastSeen = (70*Rnd(30,40))
									n\LastDist = 1
									DebugLog "seen"
									
								EndIf 								
							EndIf
							
							If n\LastDist = 0 Then 
								If n\LastSeen < 70*20 Then
									dist2# = 4.0
									For w.WayPoints = Each WayPoints
										If w\room = PlayerRoom Then
											dist2# = Distance(EntityX(Collider),EntityZ(Collider),EntityX(w\obj,True),EntityZ(w\obj,True))
											If dist2 > 0.7*((n\ID Mod 3)+1) Then
												n\EnemyX = EntityX(w\obj,True)
												n\EnemyY = EntityY(Collider)
												n\EnemyZ = EntityZ(w\obj,True)													
												Exit
											EndIf
										EndIf
									Next									
								EndIf
							EndIf
						EndIf
						
						
						If n\LastDist = 1 Then
							n\PathStatus = 0
							
							n\EnemyX = EntityX(Collider)
							n\EnemyY = EntityY(Collider)
							n\EnemyZ = EntityZ(Collider)
							
							n\PathStatus = 0
							;n\PathTimer = 0
							
							If n\State3 = 0 Then
								PlayMTFSound(MTFSFX(Rand(0,2)),n)
								n\State3 = 70*145
								n\Reload = 200
								For n2.NPCs = Each NPCs
									If n2\Target = n Then
										n2\State3 = 70*145
										n2\Reload = 200
									EndIf
								Next
								If n\Target <> Null Then n\Target\State3 = 70*145 : n\Target\Reload = 200
							EndIf
							
							angle = VectorYaw(EntityX(Collider)-EntityX(n\Collider),0,EntityZ(Collider)-EntityZ(n\Collider))
							
							RotateEntity(n\Collider, 0, CurveAngle(angle, EntityYaw(n\Collider), 10.0), 0, True)
							If n\Reload = 0 Then
								angle# = WrapAngle(angle - EntityYaw(n\Collider))
								If angle < 5 Or angle > 355 Then 
									prev% = KillTimer
									
									PlaySound2(GunshotSFX, Camera, n\Collider, 15)
									
									RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
									PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
									MoveEntity (pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
									
									Shoot(EntityX(pvt),EntityY(pvt),EntityZ(pvt),5.0/dist, False)
									n\Reload = 7
									
									DeathMSG="Subject D-9341. Died of blood loss after being shot by Nine Tailed Fox."
									
									;player killed -> "target terminated"
									If prev => 0 And KillTimer < 0 Then
										DeathMSG="Subject D-9341. Terminated by Nine Tailed Fox."
										If n\Sound <> 0 Then FreeSound n\Sound : n\Sound = 0
										n\Sound = LoadSound_Strict("SFX\MTF\Targetterminated"+Rand(1,3)+".ogg")
										PlayMTFSound(n\Sound,n)
									EndIf
								EndIf
							EndIf
							
							If AnimTime(n\obj)>958 Then
								Animate2(n\obj, AnimTime(n\obj), 1374, 1383, 0.3, False)
								If AnimTime(n\obj)=1383 Then SetAnimTime(n\obj, 78)
							Else
								If dist > 1.7 And n\Idle=0 Then
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
									Animate2(n\obj, AnimTime(n\obj), 488, 522, n\CurrSpeed*26)
								Else
									Animate2(n\obj, AnimTime(n\obj), 78, 193, 0.2, False)
									n\CurrSpeed = CurveValue(0, n\CurrSpeed, 20.0)
								EndIf
							EndIf
							
						Else ;player not visible
							If dist < 40.0 Then
								n\CurrSpeed = 0
								If n\PathTimer =< 0 Then
									n\PathStatus = FindPath(n, n\EnemyX,n\EnemyY+0.1,n\EnemyZ)
									
									If n\PathStatus = 1 Then 
										DebugLog "pathstatus 1"
										n\PathTimer = Rnd(10,15)*70
										n\State = 3
									ElseIf n\PathStatus = 2
										DebugLog "pathstatus 2"
										n\PathTimer = Rnd(15,25)*70
									Else
										DebugLog "pathstatus asdgdf"
										n\PathTimer = Rnd(5,10)*70
									EndIf
								Else
									n\PathTimer = Max(n\PathTimer-FPSfactor,0)
									
									Animate2(n\obj, AnimTime(n\obj), 78, 312, 0.2, False)
									;RotateEntity(n\Collider, Sin(MilliSecs() / 25) * 1.5, CurveAngle(n\Angle + n\PrevState + Sin(MilliSecs() / 100) * 100, EntityYaw(n\Collider), 50), 0, True)
								EndIf	
							Else
								n\LastSeen=0
							EndIf
							
							If n\LastSeen =< 0 Then ;player lost
								If n\Target = Null Then
									If n\Sound <> 0 Then FreeSound n\Sound : n\Sound = 0
									n\Sound = LoadSound_Strict("SFX\MTF\Targetlost"+Rand(1,2)+".ogg")
									PlayMTFSound(n\Sound,n)
								EndIf
								n\State = 0 : n\PathStatus = 0 : n\PathLocation = 0
								If n\Target <> Null Then
									n\State = 4
								EndIf
							EndIf
						EndIf
					Case 3 ;following a path
						
						If n\PathStatus = 2 Then
							n\State = 0
							n\CurrSpeed = 0
						ElseIf n\PathStatus = 1
							If n\Path[n\PathLocation]=Null Then 
								If n\PathLocation > 19 Then 
									n\PathLocation = 0
									n\PathStatus = 0
									If n\LastSeen > 0 Then n\State = 2 ;: n\PathTimer = 0
								Else
									n\PathLocation = n\PathLocation + 1
								EndIf
								 ;etsimässä pelaajaa, palataan takaisin "hyökkäysmoodiin"
							Else
								If n\Path[n\PathLocation]\door <> Null Then
									If n\Path[n\PathLocation]\door\open = False Then
										n\Path[n\PathLocation]\door\open = True
										n\Path[n\PathLocation]\door\timerstate = 8.0*70.0
										PlayMTFSound(MTFSFX(5),n)
									EndIf
								EndIf
								
								If dist < HideDistance*0.7 Then 
									dist2# = EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) 
									
									If Rand(5)=1 And n\Path[Min(n\PathLocation+1,19)]=Null Then 
										For n2.NPCs = Each NPCs
											If n2\NPCtype = n\NPCtype And n2<>n And n2\State<>4 Then
												If EntityDistance(n\Collider, n2\Collider)<2 Then
													If EntityDistance(n2\Collider, n\Path[n\PathLocation]\obj)<0.4 Then 
														n\Idle = 150
													EndIf
												EndIf
											EndIf
										Next
									EndIf
									
									PointEntity n\obj, n\Path[n\PathLocation]\obj
									
									RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj)+n\Angle, EntityYaw(n\Collider), 10.0*dist2), 0
									If n\Idle = 0 Then
										n\CurrSpeed = CurveValue(n\Speed*Max(Min(dist2,1.0),0.1), n\CurrSpeed, 20.0)
										MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
										
										If dist2 < (0.25+((n\Path[Min(n\PathLocation+1,19)]=Null)*0.3 * (n\ID Mod 3))) Then
											n\PathLocation = n\PathLocation + 1
										EndIf
									EndIf
								Else
									If Rand(20)=1 Then 
										PositionEntity n\Collider, EntityX(n\Path[n\PathLocation]\obj,True),EntityY(n\Path[n\PathLocation]\obj,True)+0.25,EntityZ(n\Path[n\PathLocation]\obj,True),True
										n\PathLocation = n\PathLocation + 1
										ResetEntity n\Collider
									EndIf
								EndIf
								
								
							EndIf
						Else
							n\CurrSpeed = 0
							n\State = 0
						EndIf
						
						If n\Idle = 0 And n\PathStatus = 1 Then
							If AnimTime(n\obj)>959 Then
								Animate2(n\obj, AnimTime(n\obj), 1376, 1383, 0.2, False)
								If AnimTime(n\obj)=1383 Then SetAnimTime(n\obj, 488)
							Else
								Animate2(n\obj, AnimTime(n\obj), 488, 522, n\CurrSpeed*30)
							EndIf
						Else
							If n\LastSeen > 0 Then 
								Animate2(n\obj, AnimTime(n\obj), 78, 312, 0.2, True)
							Else
								If AnimTime(n\obj)<962 Then
									If AnimTime(n\obj)>487 Then SetAnimTime(n\obj, 463)
									Animate2(n\obj, AnimTime(n\obj), 463, 487, 0.3, False)
									If AnimTime(n\obj)=487 Then SetAnimTime(n\obj, 962)
								Else
									Animate2(n\obj, AnimTime(n\obj), 962, 1259, 0.3)
								EndIf
							EndIf
								
							n\CurrSpeed = CurveValue(0, n\CurrSpeed, 20.0)
						EndIf
						
						n\Angle = CurveValue(0,n\Angle,40.0)
					Case 4 ;following another team member
						If n\Target <> Null Then
							n\Reload = 200
							PointEntity n\obj, n\Target\obj
							
							dist2# = EntityDistance(n\obj,n\Target\obj)
							
							If dist2 < 2.0 Then
								n\CurrSpeed = CurveValue(0, n\CurrSpeed, 20.0)
								RotateEntity n\obj, 0, 180, 0
							ElseIf dist2 > 7.0 Then
								If n\PathTimer =< 0 Then
									n\PathStatus = FindPath(n, EntityX(n\Target\Collider),EntityY(n\Target\Collider)+0.3,EntityZ(n\Target\Collider))
									n\PathTimer = Rnd(10.0,14.0)*70.0
									If n\PathStatus = 1 Then n\State = 3
								Else
									n\PathTimer = Max(n\PathTimer-FPSfactor,0)
								EndIf
								n\CurrSpeed = CurveValue(0, n\CurrSpeed, 20.0)
							Else 
								n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							EndIf
							
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
							
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor		
							
							If n\Target\State = 0 Then n\State = 0
						Else
							
							n\State = 0
						EndIf
						
						If Abs(n\CurrSpeed)<0.01 Then  
							If (n\ID Mod 2) = 0 Then
								n\Angle = 50
							Else
								n\Angle = -50
							EndIf
							Animate2(n\obj, AnimTime(n\obj), 78, 312, 0.2, False)
						Else
							Animate2(n\obj, AnimTime(n\obj), 488, 522, n\CurrSpeed*20)
						EndIf
						
						
					Case 5 ;shooting at some other target than the player
						target=CreatePivot()
						PositionEntity target, n\EnemyX, n\EnemyY, n\EnemyZ, True
						
						Animate2(n\obj, AnimTime(n\obj), 346, 351, 0.2, False)
						
						If Abs(EntityX(target)-EntityX(n\Collider)) < 55.0 Then
							If Abs(EntityZ(target)-EntityZ(n\Collider)) < 55.0 Then
								If Abs(EntityY(target)-EntityY(n\Collider))< 20.0 Then
									PointEntity n\obj, target
									RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),90.0), 0, True
									;PositionEntity(n\Collider, EntityX(n\Collider), CurveValue(EntityY(target)+8.0,EntityY(n\Collider),70.0), EntityZ(n\Collider))
									
									If n\PathTimer = 0 Then
										n\PathStatus = EntityVisible(n\Collider,target)
										n\PathTimer = Rand(100,200)
									Else
										n\PathTimer = Min(n\PathTimer-FPSfactor,0.0)
									EndIf
									
									If n\PathStatus = 1 Then
										If n\Reload =< 0 Then
											dist# = Distance(EntityX(target),EntityZ(target),EntityX(n\Collider),EntityZ(n\Collider))
											
											If dist<20.0 Then
												PositionEntity pvt, EntityX(n\obj),EntityY(n\obj), EntityZ(n\obj)
												RotateEntity pvt, EntityPitch(n\Collider), EntityYaw(n\Collider),0
												MoveEntity (pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
												
												If WrapAngle(EntityYaw(pvt)-EntityYaw(n\Collider))<5 Then
													;RotateEntity(p\pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0)
													;MoveEntity(pvt, 0.0, 0.80, 0.45)
													PlaySound2(GunshotSFX, Camera, n\Collider, 20)
													p.Particles = CreateParticle(EntityX(n\obj, True), EntityY(n\obj, True), EntityZ(n\obj, True), 1, 0.2, 0.0, 5)
													PositionEntity(p\pvt, EntityX(pvt), EntityY(pvt), EntityZ(pvt))
													
													
													n\Reload = 10
												EndIf
												
											EndIf
										EndIf
									EndIf
									
								EndIf
							EndIf
						EndIf		
						
						FreeEntity target
					Case 7 ;173 spotted
						If Curr173\Idle = 2 Then
							If Curr173\Target = Null Then
								Curr173\Target = n
								Curr173\PathStatus = 0
							ElseIf Curr173\Target <> n
								If EntityDistance(n\Collider, Curr173\Collider) < EntityDistance(Curr173\Target\Collider, Curr173\Collider) Then
									Curr173\Target = n
									Curr173\PathStatus = 0
								EndIf
							EndIf
							
							If n\PathStatus <> 1 And n\PathTimer=<0 Then
								For r.Rooms = Each Rooms
									If r\RoomTemplate\Name = "start" Then
										n\PathStatus = FindPath(n, EntityX(r\obj,True)+1024*RoomScale,EntityY(r\obj,True)+0.4,EntityZ(r\obj,True)+384*RoomScale)
										If n\PathStatus = 2 Then
											n\PathTimer = 70*10
											n\State = 10
										Else
											n\PathTimer = 70*5
											n\State = 3	
										EndIf
										
										Exit
									EndIf
								Next
							Else
								n\State = 3
							EndIf
						ElseIf Curr173\Idle < 3
							Animate2(n\obj, AnimTime(n\obj), 165, 256, 0.5)
							PointEntity n\obj, Curr173\Collider
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
							
							If n\Reload=<0 Then
								PlayMTFSound(LoadTempSound("SFX\MTF\173blinking.ogg"),n)
								n\Reload = n\Reload+Rand(70*5,70*10)
								
								If dist > 8.0 And Rand(4)=1 Then
									Curr173\Idle = 2
								EndIf
							EndIf
						EndIf
						
						Curr173\Idle = Max(1,Curr173\Idle)
					Default
						n\PathStatus = 0
						n\CurrSpeed = 0
						
						If AnimTime(n\obj)<962 Then
							If AnimTime(n\obj)>487 Then SetAnimTime(n\obj, 463)
							Animate2(n\obj, AnimTime(n\obj), 463, 487, 0.2, False)
							If AnimTime(n\obj)=487 Then SetAnimTime(n\obj, 962)
						Else
							Animate2(n\obj, AnimTime(n\obj), 962, 1259, 0.3)
						EndIf
						
						n\PathTimer = Max(n\PathTimer-FPSfactor,0)
						
						;RotateEntity(n\Collider, Sin(MilliSecs() / 25) * 1.5, CurveAngle(n\Angle + n\PrevState + Sin(MilliSecs() / 100) * 100, EntityYaw(n\Collider), 50), 0, True)
				End Select
				
				;loop the breathing sound if the npc isn't talking
				If (Not ChannelPlaying(n\SoundChn)) Then
					n\SoundChn2 = LoopSound2(MTFSFX(6),n\SoundChn2,Camera,n\Collider,6.0)
				ElseIf (n\SoundChn2 <> 0)
					If ChannelPlaying(n\SoundChn2) Then StopChannel n\SoundChn2
				EndIf
				
				n\LastSeen = Max(n\LastSeen-FPSfactor,0)
				
				If n\LastSeen<70*20 And n\LastSeen+FPSfactor=>70*20 Then
					If Rand(2)=1 Then 
						If n\Sound <> 0 Then FreeSound n\Sound : n\Sound = 0
						n\Sound = LoadSound_Strict("SFX\MTF\Searching"+Rand(1,3)+".ogg")
						PlayMTFSound(n\Sound,n)
					EndIf
				EndIf
				
				n\State2=Max(n\State2-FPSfactor,0)
				
				n\State3=Max(n\State3-FPSfactor,0)
				If n\Target = Null Then 
					If n\State3 < 70*120 And n\State3+FPSfactor => 70*120  And FPSfactor <> 0 Then
						PlaySound2 (MTFSFX(Rand(3,4)),Camera,n\Collider, 8.0)
					EndIf
				EndIf
				
				n\PrevY = Max(0,n\PrevY-FPSfactor)
				
				If n\CurrSpeed > 0.01 Then
					If prevframe > 500 And AnimTime(n\obj)<495 Then
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
					ElseIf prevframe < 505 And AnimTime(n\obj)=>505
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
					;ElseIf prevframe < 408 And AnimTime(n\obj)=>408
					;	PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))						
					;ElseIf prevframe < 423 And AnimTime(n\obj)=>423
					;	PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))
					EndIf
				EndIf
				
				If n\State <> 5 Then 
					If Rand(10)=1 Then 
						If EntityY(n\Collider)<-20.0 Then
							For r.Rooms = Each Rooms
								If Abs(EntityX(n\Collider)-EntityX(r\obj))<4.0 Then
									If Abs(EntityZ(n\Collider)-EntityZ(r\obj))<4.0 Then
										PositionEntity n\Collider, EntityX(r\obj), 0.5, EntityZ(r\obj)
										ResetEntity n\Collider
										Exit
									EndIf
								EndIf
							Next
						EndIf
						
						For d.Doors = Each Doors
							If d\open = False And d\Code="" Then
								If Abs(EntityX(d\obj,True)-EntityX(n\Collider))<1.5 Then
									If Abs(EntityZ(d\obj,True)-EntityZ(n\Collider))<1.5 Then
										UseDoor(d, False)
										Exit
									EndIf
								EndIf
							EndIf
						Next							
						
						If n\PrevY = 0 Then 
							If Curr106\State < -10 Then 
								If Abs(EntityX(n\Collider)-EntityX(Curr106\Collider))<4.0 Then
									If Abs(EntityZ(n\Collider)-EntityZ(Curr106\Collider))<4.0 Then
									;n\PrevY = 70*30
										PlayMTFSound(LoadTempSound("SFX\MTF\Oldman3.ogg"),n)
										For n2.NPCs = Each NPCs
											If n2\NPCtype = NPCtypeMTF Then
												n2\PrevY = 70*30
											EndIf
										Next
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf		
					
					If Rand(10)=1 Then 
						
						Local IgnorePlayer=False
						If 0 Then 
							If PlayerRoom\RoomTemplate\Name="gatea" And Contained106 Then
								IgnorePlayer = True
							ElseIf Curr173\Idle = 2  ; 173 is in its "recontainment box"
								n\State=7
							ElseIf Curr173\Idle < 3
								;check if 173 is visible
								Curr173\Idle=False
								If EntityDistance(n\Collider, Curr173\Collider)<5.0 Then
									If EntityVisible(n\Collider, Curr173\Collider) Then
										If n\PrevY=<0 Then
											n\Reload = 600
											If n\State=2 Then ;if chasing the player, play the "stop chasing the d" -clip
												TempSound=LoadTempSound("SFX\MTF\173spotted2.ogg")
											Else
												TempSound=LoadTempSound("SFX\MTF\173spotted1.ogg")	
											EndIf
											PlayMTFSound(TempSound,n)
											
											For n2.NPCs = Each NPCs
												If n2\NPCtype = NPCtypeMTF Then n2\PrevY = 70*30 : n2\Reload = Rand(100,200)
											Next
										EndIf
										
										n\State=7
									EndIf
								EndIf
								
								;173 spotted -> don't pay any attention to the player UNLESS there's someone else looking at 173
								If n\State=7 Then
									IgnorePlayer = True
									For n2.NPCs = Each NPCs
										If n2\NPCtype = NPCtypeMTF Then
											If n<>n2 Then
												If n2\State=7 Then IgnorePlayer = False : Exit
											EndIf
										EndIf
									Next
								EndIf
								
							EndIf
							
						EndIf
						
						If (Not IgnorePlayer) And n\State<>2 Then 
							;check if player is visible
							temp = False
							If dist < PlayerSoundVolume Then 
								temp = True 
							ElseIf dist < 8.0-(CrouchState*3)+LightVolume
								;PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider) + 0.6, EntityZ(n\Collider))
								;PointEntity(pvt, Collider)
								angle# = WrapAngle(DeltaYaw(n\Collider, Collider));-EntityYaw(n\Collider))
								If angle<90+40*(n\LastSeen>0) Or angle>270-40*(n\LastSeen>0) Then
									If EntityVisible(n\Collider,Collider) Then temp = True : n\LastDist = 1
								EndIf
							EndIf
							
							If temp Then 
								n\LastSeen = (70*Rnd(30,35))
								n\State = 2
								n\State2 = 10
								n\EnemyX = EntityX(Collider)
								n\EnemyY = EntityY(Collider)
								n\EnemyZ = EntityZ(Collider)
								
								For n2.NPCs = Each NPCs
									If n <> n2 And n2\NPCtype = NPCtypeMTF Then
										If Abs(EntityX(n\Collider)-EntityX(n2\Collider))<32.0 Then
											If Abs(EntityZ(n\Collider)-EntityZ(n2\Collider))<32.0 Then		
												n2\LastSeen = (70*Rnd(30,35))
												;n\LastDist = 1
												n2\State = 2
												n2\State2 = 20
												n2\EnemyX = EntityX(Collider)
												n2\EnemyY = EntityY(Collider)
												n2\EnemyZ = EntityZ(Collider)
											EndIf
										EndIf
									EndIf
								Next						
							EndIf
							
						EndIf ;/if (not ignoreplayer)
						
					EndIf ;/if rand(10)=1
					
					
				EndIf
				
				FreeEntity pvt
				
				n\Reload = Max(0, n\Reload - FPSfactor)
				
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
				
				RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), 0
				;[End Block]
			Case NPCtypeD 	;------------------------------------------------------------------------------------------------------------------
				;[Block]
				RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
				
				prevframe = AnimTime(n\obj)
				
				Select n\State
					Case 0 ;idle
						n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
						Animate2(n\obj, AnimTime(n\obj), 210, 235, 0.1)
					Case 1 ;walking
						n\CurrSpeed = CurveValue(0.015, n\CurrSpeed, 5.0)
						Animate2(n\obj, AnimTime(n\obj), 236, 260, n\CurrSpeed * 18)
					Case 2 ;running
						n\CurrSpeed = CurveValue(0.03, n\CurrSpeed, 5.0)
						Animate2(n\obj, AnimTime(n\obj), 301, 319, n\CurrSpeed * 18)
				End Select
				
				If n\CurrSpeed > 0.01 Then
					If prevframe < 244 And AnimTime(n\obj)=>244 Then
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))						
					ElseIf prevframe < 256 And AnimTime(n\obj)=>256
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					ElseIf prevframe < 309 And AnimTime(n\obj)=>309
						PlaySound2(StepSFX(2,1,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					ElseIf prevframe =< 319 And AnimTime(n\obj)=<301
						PlaySound2(StepSFX(2,1,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
					EndIf
				EndIf
				
				MoveEntity(n\Collider, 0, 0, n\CurrSpeed * FPSfactor)
				
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
				
				RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider)-180.0, 0
				;[End Block]
			Case NPCtype5131
				;[Block}
				If KeyHit(48) Then n\Idle = True : n\State2 = 0
				
				
				If PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then 
					If n\Idle Then
						HideEntity(n\obj)
						HideEntity(n\obj2)
						If Rand(200)=1 Then
							For w.WayPoints = Each WayPoints
								If w\room<>PlayerRoom Then
									x = Abs(EntityX(Collider)-EntityX(w\obj,True))
									If x>3 And x < 9 Then
										z = Abs(EntityZ(Collider)-EntityZ(w\obj,True))
										If z>3 And z < 9 Then
											PositionEntity(n\Collider, EntityX(w\obj,True), EntityY(w\obj,True), EntityZ(w\obj,True))
											PositionEntity(n\obj, EntityX(w\obj,True), EntityY(w\obj,True), EntityZ(w\obj,True))
											ResetEntity n\Collider
											ShowEntity(n\obj)
											ShowEntity(n\obj2)
											
											n\LastSeen = 0
											
											n\Path[0]=w
											
											n\Idle = False
											n\State2 = Rand(15,20)*70
											n\State = Max(Rand(-1,2),0)
											Exit
										EndIf
									EndIf
								EndIf
							Next
						End If
					Else
						dist = EntityDistance(Collider, n\Collider)
						
						;RotateEntity n\obj, 0, EntityYaw(n\Collider), ((MilliSecs()/5) Mod 360)
						
						;Animate2(n\obj, AnimTime(n\obj), 32, 113, 0.4)
						
						PositionEntity n\obj2, EntityX(n\Collider)+Rnd(-0.05,0.05), EntityY(n\Collider) - 0.2 + Sin((MilliSecs()/8-45) Mod 360)*0.05, EntityZ(n\Collider)+Rnd(-0.05,0.05)
						RotateEntity n\obj2, 0, EntityYaw(n\obj), 0
						SetAnimTime n\obj2, AnimTime(n\obj)
						
						If n\State = 0 Then
							Animate2(n\obj, AnimTime(n\obj), 229, 299, 0.2)
							
							If n\LastSeen Then 	
								PointEntity n\obj2, Collider
								RotateEntity n\obj, 0, CurveAngle(EntityYaw(n\obj2),EntityYaw(n\obj),40), 0
								If dist < 4 Then n\State = Rand(1,2)
							Else
								If dist < 6 And Rand(5)=1 Then
									If EntityInView(n\Collider,Camera) Then
										If EntityVisible(Collider, n\Collider) Then
											n\LastSeen = 1
											PlaySound LoadTempSound("SFX\Bell"+Rand(2,3)+".ogg")
										EndIf
									EndIf
								EndIf								
							EndIf
							
						Else
							If n\Path[0]=Null Then
								
							;move towards a waypoint that is:
							;1. max 8 units away from 513-1
							;2. further away from the player than 513-1's current position 
								For w.WayPoints = Each WayPoints
									x = Abs(EntityX(n\Collider,True)-EntityX(w\obj,True))
									If x < 8.0 And x > 1.0 Then
										z = Abs(EntityZ(n\Collider,True)-EntityZ(w\obj,True))
										If z < 8.0 And z > 1.0 Then
											If EntityDistance(Collider, w\obj) > dist Then
												n\Path[0]=w
												Exit
											EndIf
										EndIf
									EndIf
								Next
								
								;no suitable path found -> 513-1 simply disappears
								If n\Path[0] = Null Then
									n\Idle = True
									n\State2 = 0
								EndIf
							Else
								
								If EntityDistance(n\Collider, n\Path[0]\obj) > 1.0 Then
									PointEntity n\obj, n\Path[0]\obj
									RotateEntity n\Collider, CurveAngle(EntityPitch(n\obj),EntityPitch(n\Collider),15.0), CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),15.0), 0, True
									n\CurrSpeed = CurveValue(0.05*Max((7.0-dist)/7.0,0.0),n\CurrSpeed,15.0)
									MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
									If Rand(200)=1 Then MoveEntity n\Collider, 0, 0, 0.5
									RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True
								Else
									For i = 0 To 4
										If n\Path[0]\connected[i] <> Null Then
											If EntityDistance(Collider, n\Path[0]\connected[i]\obj) > dist Then
												
												If n\LastSeen = 0 Then 
													If EntityInView(n\Collider,Camera) Then
														If EntityVisible(Collider, n\Collider) Then
															n\LastSeen = 1
															PlaySound LoadTempSound("SFX\Bell"+Rand(2,3)+".ogg")
														EndIf
													EndIf
												EndIf
												
												n\Path[0]=n\Path[0]\connected[i]
												Exit
											EndIf
										EndIf
									Next
									
									If n\Path[0]=Null Then n\State2 = 0
								EndIf
							EndIf
						EndIf
						
						PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2 + Sin((MilliSecs()/8) Mod 360)*0.1, EntityZ(n\Collider))
						
						Select n\State 
							Case 1
								Animate2(n\obj, AnimTime(n\obj), 458, 527, n\CurrSpeed*20)
								RotateEntity n\obj, 0, EntityYaw(n\Collider), 0 
							Case 2
								Animate2(n\obj, AnimTime(n\obj), 229, 299, 0.2)
								RotateEntity n\obj, 0, EntityYaw(n\Collider), 0						
						End Select
						
						If n\State2 > 0 Then
							If dist < 4.0 Then n\State2 = n\State2-FPSfactor*4
							n\State2 = n\State2-FPSfactor
						Else
							n\Path[0]=Null
							n\Idle = True
							n\State2=0
						EndIf
						
					End If
					
				EndIf
				
				n\DropSpeed = 0
				ResetEntity(n\Collider)						
				;[End Block]
			Case NPCtype372 ;------------------------------------------------------------------------------------------------------------------
				;[Block]
				If PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then 
					If n\Idle Then
						HideEntity(n\obj)
						If Rand(50) = 1 And (BlinkTimer < -5 And BlinkTimer > -15) Then
							ShowEntity(n\obj)
							angle# = EntityYaw(Collider)+Rnd(-90,90)
							
							dist = Rnd(1.5, 2.0)
							PositionEntity(n\Collider, EntityX(Collider) + Sin(angle) * dist, EntityY(Collider)+0.2, EntityZ(Collider) + Cos(angle) * dist)
							n\Idle = False
							n\State = Rand(20, 60)
							
							If Rand(300)=1 Then PlaySound2(RustleSFX(Rand(0,2)),Camera, n\obj, 8, Rnd(0.0,0.2))
						End If
					Else
						PositionEntity(n\obj, EntityX(n\Collider) + Rnd(-0.005, 0.005), EntityY(n\Collider)+0.3+0.1*Sin(MilliSecs()/2), EntityZ(n\Collider) + Rnd(-0.005, 0.005))
						RotateEntity n\obj, 0, EntityYaw(n\Collider), ((MilliSecs()/5) Mod 360)
						
						Animate2(n\obj, AnimTime(n\obj), 32, 113, 0.4)
						
						If EntityInView(n\obj, Camera) Then
							Achievements(Achv372) = True
							
							If Rand(30)=1 Then 
								If (Not ChannelPlaying(n\SoundChn)) Then
									If EntityVisible(Camera, n\obj) Then 
										n\SoundChn = PlaySound2(RustleSFX(Rand(0,2)),Camera, n\obj, 8, 0.3)
									EndIf
								EndIf
							EndIf
							
							temp = CreatePivot()
							PositionEntity temp, EntityX(Collider), EntityY(Collider), EntityZ(Collider)
							PointEntity temp, n\Collider
							
							angle =  WrapAngle(EntityYaw(Collider)-EntityYaw(temp))
							If angle < 180 Then
								RotateEntity n\Collider, 0, EntityYaw(Collider)-80, 0		
							Else
								RotateEntity n\Collider, 0, EntityYaw(Collider)+80, 0
							EndIf
							FreeEntity temp
							
							MoveEntity n\Collider, 0, 0, 0.03*FPSfactor
							
							n\State = n\State-FPSfactor
						EndIf
						n\State=n\State-(FPSfactor/80.0)
						If n\State <= 0 Then n\Idle = True	
					End If
					
				EndIf
				
				n\DropSpeed = 0
				ResetEntity(n\Collider)						
				;[End Block]
			Case NPCtypeApache ;------------------------------------------------------------------------------------------------------------------
				;[Block]
				dist = EntityDistance(Collider, n\Collider)
				If dist<60.0 Then 
					If PlayerRoom\RoomTemplate\Name = "exit1" Then 
						dist2 = Max(Min(EntityDistance(n\Collider, PlayerRoom\Objects[3])/(8000.0*RoomScale),1.0),0.0)
					Else 
						dist2 = 1.0
					EndIf
					
					n\SoundChn = LoopSound2(ApacheSFX, n\SoundChn, Camera, n\Collider, 25.0, dist2)
				EndIf
				
				n\DropSpeed = 0
				
				Select n\State
					Case 0,1
						TurnEntity(n\obj2,0,20.0*FPSfactor,0)
						TurnEntity(n\obj3,20.0*FPSfactor,0,0)
						
						If n\State=1 Then
							If Abs(EntityX(Collider)-EntityX(n\Collider))< 30.0 Then
								If Abs(EntityZ(Collider)-EntityZ(n\Collider))<30.0 Then
									If Abs(EntityY(Collider)-EntityY(n\Collider))<20.0 Then
										If Rand(20)=1 Then 
											If EntityVisible(Collider, n\Collider) Then
												n\State = 2
												PlaySound2(AlarmSFX(2), Camera, n\Collider, 50, 1.0)
											EndIf
										EndIf									
									EndIf
								EndIf
							EndIf							
						EndIf
					Case 2,3 ;player located -> attack
						
						If n\State = 2 Then 
							target = Collider
						ElseIf n\State = 3
							target=CreatePivot()
							PositionEntity target, n\EnemyX, n\EnemyY, n\EnemyZ, True
						EndIf
						
						TurnEntity(n\obj2,0,20.0*FPSfactor,0)
						TurnEntity(n\obj3,20.0*FPSfactor,0,0)
						
						If Abs(EntityX(target)-EntityX(n\Collider)) < 55.0 Then
							If Abs(EntityZ(target)-EntityZ(n\Collider)) < 55.0 Then
								If Abs(EntityY(target)-EntityY(n\Collider))< 20.0 Then
									PointEntity n\obj, target
									RotateEntity n\Collider, CurveAngle(Min(WrapAngle(EntityPitch(n\obj)),40.0),EntityPitch(n\Collider),40.0), CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),90.0), EntityRoll(n\Collider), True
									PositionEntity(n\Collider, EntityX(n\Collider), CurveValue(EntityY(target)+8.0,EntityY(n\Collider),70.0), EntityZ(n\Collider))
									
									dist# = Distance(EntityX(target),EntityZ(target),EntityX(n\Collider),EntityZ(n\Collider))
									
									n\CurrSpeed = CurveValue(Min(dist-6.5,6.5)*0.008, n\CurrSpeed, 50.0)
									
									;If Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\collider),EntityZ(n\collider)) > 6.5 Then
									;	n\currspeed = CurveValue(0.08,n\currspeed,50.0)
									;Else
									;	n\currspeed = CurveValue(0.0,n\currspeed,30.0)
									;EndIf
									MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
									
									
									If n\PathTimer = 0 Then
										n\PathStatus = EntityVisible(n\Collider,target)
										n\PathTimer = Rand(100,200)
									Else
										n\PathTimer = Min(n\PathTimer-FPSfactor,0.0)
									EndIf
									
									If n\PathStatus = 1 Then ;player visible
										RotateEntity n\Collider, EntityPitch(n\Collider), EntityYaw(n\Collider), CurveAngle(0, EntityRoll(n\Collider),40), True
										
										If n\Reload =< 0 Then
											If dist<20.0 Then
												pvt = CreatePivot()
												
												PositionEntity pvt, EntityX(n\Collider),EntityY(n\Collider), EntityZ(n\Collider)
												RotateEntity pvt, EntityPitch(n\Collider), EntityYaw(n\Collider),EntityRoll(n\Collider)
												MoveEntity pvt, 0, 8.87*(0.21/9.0), 8.87*(1.7/9.0) ;2.3
												PointEntity pvt, target
												
												If WrapAngle(EntityYaw(pvt)-EntityYaw(n\Collider))<10 Then
													PlaySound2(Gunshot2SFX, Camera, n\Collider, 20)
													
													DeathMSG = "''CH-2 to control. Shot down a runaway Class D at Gate B.''"
													
													Shoot( EntityX(pvt),EntityY(pvt), EntityZ(pvt),15/dist,(n\State=2))
													
													n\Reload = 5
												EndIf
												
												FreeEntity pvt
											EndIf
										EndIf
									Else 
										RotateEntity n\Collider, EntityPitch(n\Collider), EntityYaw(n\Collider), CurveAngle(-20, EntityRoll(n\Collider),40), True
									EndIf
									MoveEntity n\Collider, -EntityRoll(n\Collider)*0.002,0,0
									
									n\Reload=n\Reload-FPSfactor
									
									
								EndIf
							EndIf
						EndIf		
						
						If n\State = 3 Then FreeEntity target
					Case 4 ;crash
						If n\State2 < 300 Then
							
							TurnEntity(n\obj2,0,20.0*FPSfactor,0)
							TurnEntity(n\obj3,20.0*FPSfactor,0,0)
							
							TurnEntity n\Collider,0,-FPSfactor*7,0;Sin(MilliSecs()/40)*FPSfactor
							n\State2=n\State2+FPSfactor*0.3
							
							target=CreatePivot()
							PositionEntity target, n\EnemyX, n\EnemyY, n\EnemyZ, True
							
							PointEntity n\obj, target
							MoveEntity n\obj, 0,0,FPSfactor*0.001*n\State2
							PositionEntity(n\Collider, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							
							If EntityDistance(n\obj, target) <0.3 Then
								If TempSound2 <> 0 Then FreeSound TempSound2 : TempSound2 = 0
								TempSound2 = LoadSound_Strict("SFX\apachecrash2.ogg")
								CameraShake = Max(CameraShake, 3.0)
								PlaySound TempSound2
								n\State = 5
							EndIf
							
							FreeEntity target
						EndIf
				End Select
				
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
				RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider), EntityRoll(n\Collider), True
				;[End Block]
			Case NPCtypeTentacle
				;[Block]
				dist = EntityDistance(n\Collider,Collider)
				
				If dist < 8.0 Then 
					
					DebugLog "tentacle: "+n\State+" - "+ AnimTime(n\obj)
					
					Select n\State 
						Case 0 ;spawn
							
							If AnimTime(n\obj)>283 Then
								HeartBeatVolume = Max(CurveValue(1.0, HeartBeatVolume, 50),HeartBeatVolume)
								HeartBeatRate = Max(CurveValue(130, HeartBeatRate, 100),HeartBeatRate)
								
								PointEntity n\obj, Collider
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
								
								Animate2(n\obj, AnimTime(n\obj), 283, 389, 0.3, False)
								
								If AnimTime(n\obj)>388 Then n\State = 1
							Else
								If dist < 2.5 Then 
									SetAnimTime(n\obj, 284)
									n\Sound2 = LoadSound_Strict("SFX\035\tentaclespawn.ogg")
									PlaySound(n\Sound2)
								EndIf
							EndIf
							;spawn 283,389
							;attack 2, 32
							;idle 33, 174
						Case 1 ;idle
							If dist < 1.8 Then 
								If Abs(DeltaYaw(n\Collider, Collider))<20 Then 
									n\State = 2
									If n\Sound<>0 Then FreeSound n\Sound : n\Sound = 0 
									If n\Sound2<>0 Then FreeSound n\Sound2 : n\Sound2 = 0 
									
								EndIf
							EndIf
							
							PointEntity n\obj, Collider
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
							
							Animate2(n\obj, AnimTime(n\obj), 33, 174, 0.3, True)
						Case 2
							
							;finish the idle animation before playing the attack animation
							If AnimTime(n\obj)>33 And AnimTime(n\obj)<174 Then
								Animate2(n\obj, AnimTime(n\obj), 33, 174, 2.0, False)
							Else
								PointEntity n\obj, Collider
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0							
								
								If AnimTime(n\obj)>33 Then 
									SetAnimTime(n\obj,2)
									n\Sound = LoadSound_Strict("SFX\035\tentacleattack"+Rand(1,2)+".ogg")
									PlaySound(n\Sound)
								EndIf
								Animate2(n\obj, AnimTime(n\obj), 2, 32, 0.3, False)
								
								If AnimTime(n\obj)>=5 And AnimTime(n\obj)<6 Then
									If dist < 1.8 Then
										If Abs(DeltaYaw(n\Collider, Collider))<20 Then 
											If WearingHazmat Then
												Injuries = Injuries+Rnd(0.5)
												PlaySound(LoadTempSound("SFX\bodyfall.ogg"))
											Else
												BlurTimer = 100
												Injuries = Injuries+Rnd(1.0,1.5)
												PlaySound DeathSFX(3)
												
												If Injuries > 3.0 Then 
													DeathMSG = "''We'll need more than the regular cleaning team to care of this. "
													DeathMSG = DeathMSG + "Two large and highly active tentacle-like appendages seem "
													DeathMSG = DeathMSG + "to have formed inside the chamber. Their level of their aggression "
													DeathMSG = DeathMSG + "is unlike anything we've seen before - it looks like they've "
													DeathMSG = DeathMSG + "beaten some unfortunate Class D to death at some point during the breach.''"
													Kill()
												EndIf
											EndIf
											
										EndIf
									EndIf
									
									SetAnimTime(n\obj, 6)
								ElseIf AnimTime(n\obj)=32
									n\State = 1
									SetAnimTime(n\obj, 173)
								EndIf
							EndIf
							
					End Select
					
					PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
					RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider)-180, EntityRoll(n\Collider), True
					
					n\DropSpeed = 0
					
					ResetEntity n\Collider
					
				EndIf
				;[End Block]
			Case NPCtype860
				;[Block]
				If PlayerRoom\RoomTemplate\Name = "room860" Then
					Local fr.Forest=Object.Forest(PlayerRoom\Objects[1])
					
					dist = EntityDistance(Collider,n\Collider)
					
					Select n\State
						Case 0 ;idle (hidden)
							
							HideEntity n\Collider
							HideEntity n\obj
							HideEntity n\obj2
							
							n\State2 = 0
							PositionEntity(n\Collider, 0, -100, 0)
						Case 1 ;appears briefly behind the trees
							n\DropSpeed = 0
							
							If EntityY(n\Collider)<= -100 Then
								TFormPoint(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,PlayerRoom\obj)
								
								x = Floor((TFormedX()*RoomScale+6.0)/12.0)
								z = Floor((TFormedZ()*RoomScale+6.0)/12.0)
								
								TFormPoint(x/RoomScale*12.0,0,z/RoomScale*12.0,fr\Forest_Pivot,0)
								
								DebugLog "player x, z: "+x+", "+z
								DebugLog fr\grid[(z*gridsize)+x]
								
								For x2 = Max(x-1,0) To Min(x+1,gridsize) Step 2
									For z2 = Max(z-1,0) To Min(z+1,gridsize) Step 2
										If fr\grid[(z2*gridsize)+x2]=0 Then
											
											TFormPoint((x*12 + (x2-x)*6)/RoomScale,0,(z*12 + (z2-z)*6)/RoomScale,PlayerRoom\obj,0)
											
											PositionEntity n\Collider, TFormedX(), EntityY(fr\Forest_Pivot,True)+2.3, TFormedZ()
											DebugLog ("positioned at "+x2+", "+z2)
											DebugLog TFormedX()+", "+ (EntityY(fr\Forest_Pivot,True)+2.3)+", "+TFormedZ()
											
											If EntityInView(n\Collider, Camera) Then
												PositionEntity n\Collider, 0, -110, 0
											Else ;only spawn the monster outside the player's field of view
												x2 = Min(x+1,gridsize)
												Exit												
											EndIf
											
										EndIf
									Next
								Next
								
								If EntityY(n\Collider)> -100 Then
									PlaySound2(Step2SFX(Rand(3,5)), Camera, n\Collider, 15.0, 0.5)
									
									Select Rand(3)
										Case 1
											DebugLog "1"
											PointEntity n\Collider, Collider
											SetAnimTime(n\obj, 2)
										Case 2
											DebugLog "2"
											PointEntity n\Collider, Collider
											SetAnimTime(n\obj, 201)
										Case 3
											DebugLog "3"
											PointEntity n\Collider, Collider
											TurnEntity n\Collider, 0, 90, 0
											SetAnimTime(n\obj, 299)
									End Select
									
									n\State2 = 0
								EndIf
							Else
								
								ShowEntity n\obj
								ShowEntity n\Collider
								
								PositionEntity n\Collider, EntityX(n\Collider), EntityY(fr\Forest_Pivot,True)+2.3, EntityZ(n\Collider)
								
								If n\State2 = 0 Then ;don't start moving until the player is looking
									If EntityInView(n\Collider, Camera) Then 
										n\State2 = 1
										DebugLog "player is looking"
										If Rand(8)=1 Then
											TempSound = LoadTempSound("SFX\860\Forestmonster"+Rand(0,2)+".ogg")
											PlaySound2(TempSound, Camera, n\Collider, 20.0)
										EndIf										
									EndIf
								Else
									If AnimTime(n\obj)<=199 Then
										Animate2(n\obj, AnimTime(n\obj), 2, 199, 0.5,False)
										If AnimTime(n\obj)=199 Then SetAnimTime(n\obj,298) : PlaySound2(Step2SFX(Rand(3,5)), Camera, n\Collider, 15.0)
									ElseIf AnimTime(n\obj) <= 297
										PointEntity n\Collider, Collider
										Animate2(n\obj, AnimTime(n\obj), 200, 297, 0.5,False)
										If AnimTime(n\obj)=297 Then SetAnimTime(n\obj,298) : PlaySound2(Step2SFX(Rand(3,5)), Camera, n\Collider, 15.0)
									Else
										angle = CurveAngle(point_direction(EntityX(n\Collider),EntityZ(n\Collider),EntityX(Collider),EntityZ(Collider)),EntityYaw(n\Collider)+90,20.0)
										
										RotateEntity n\Collider, 0, angle-90, 0, True
										
										Animate2(n\obj, AnimTime(n\obj), 298, 316, n\CurrSpeed*10)
										
										n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 10.0)
										MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
										DebugLog "speed: "+n\CurrSpeed +" -> "+n\Speed
										
										If dist>15.0 Then
											PositionEntity n\Collider, 0,-110,0
											n\State = 0
											n\State2 = 0
										EndIf
									EndIf									
								EndIf
								
							EndIf
							
							ResetEntity n\Collider
						Case 2 ;appears on the path and starts to walk towards the player
							ShowEntity n\obj
							ShowEntity n\Collider
							
							prevframe = AnimTime(n\obj)
							
							If EntityY(n\Collider)<= -100 Then
								TFormPoint(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,PlayerRoom\obj)
								x = Floor((TFormedX()*RoomScale+6.0)/12.0)
								z = Floor((TFormedZ()*RoomScale+6.0)/12.0)
								
								For x2 = Max(x-1,0) To Min(x+1,gridsize)
									For z2 = Max(z-1,0) To Min(z+1,gridsize)
										If fr\grid[(z2*gridsize)+x2]>0 And (x2<>x Or z2<>z) And (x2=x Or z2=z) Then
											
											TFormPoint((x2*12)/RoomScale,0,(z2*12)/RoomScale,PlayerRoom\obj,0)
											
											PositionEntity n\Collider, TFormedX(), EntityY(fr\Forest_Pivot,True)+0.5, TFormedZ()
											DebugLog ("positioned at "+x2+", "+z2)
											DebugLog TFormedX()+", "+ (EntityY(fr\Forest_Pivot,True)+2.3)+", "+TFormedZ()
											
											If EntityInView(n\Collider, Camera) Then
												DebugLog "blink"
												BlinkTimer=-10
											Else
												x2 = Min(x+1,gridsize)
												Exit
											EndIf
										EndIf
									Next
								Next
							Else
								
								angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\Collider)+90,80.0)
								
								RotateEntity n\Collider, 0, angle-90, 0, True
								
								n\CurrSpeed = CurveValue(n\Speed*0.3, n\CurrSpeed, 50.0)
								MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
								
								Animate2(n\obj, AnimTime(n\obj), 494, 569, n\CurrSpeed*25)
								
								If n\State2 = 0 Then
									If dist<8.0 Then
										If EntityInView(n\Collider,Camera) Then
											If TempSound2<>0 Then FreeSound TempSound2 : TempSound2 = 0
											TempSound2 = LoadSound("SFX\860\Horror"+Rand(1,2)+".ogg")
											PlaySound TempSound2
											
											TempSound = LoadTempSound("SFX\860\Forestmonster"+Rand(0,2)+".ogg")
											PlaySound2(TempSound, Camera, n\Collider)	
											n\State2 = 1
										EndIf										
									EndIf
								EndIf
								
								If CurrSpeed > 0.03 Then ;the player is running
									DebugLog "running: "+n\State3
									n\State3 = n\State3 + FPSfactor
									If Rnd(5000)<n\State3 Then
										temp = True
										If n\SoundChn <> 0 Then
											If ChannelPlaying (n\SoundChn) Then temp = False
										EndIf
										If temp Then 
											TempSound = LoadTempSound("SFX\860\Forestmonster"+Rand(0,2)+".ogg")
											n\SoundChn = PlaySound2(TempSound, Camera, n\Collider)
										EndIf
									EndIf
								Else
									n\State3 = Max(n\State3 - FPSfactor,0)
								EndIf
								
								If dist<4.5 Or n\State3 > Rnd(200,250) Then 
									TempSound = LoadTempSound("SFX\860\Forestmonster"+Rand(3,5)+".ogg")
									n\SoundChn = PlaySound2(TempSound, Camera, n\Collider)
									n\State = 3
								EndIf
								
								If dist > 16.0 Then
									n\State = 0
									n\State2 = 0
									PositionEntity n\Collider, 0,-110,0
								EndIf
							EndIf
							
							;535, 568
							If (prevframe < 533 And AnimTime(n\obj)=>533) Or (prevframe > 568 And AnimTime(n\obj)<2) Then
								PlaySound2(Step2SFX(Rand(3,5)), Camera, n\Collider, 15.0, 0.6)
							EndIf
							
						Case 3 ;runs towards the player and attacks
							ShowEntity n\obj
							ShowEntity n\Collider
							
							prevframe = AnimTime(n\obj)
							
							angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\Collider)+90,40.0)
							
							RotateEntity n\Collider, 0, angle-90, 0, True
							
							If n\Sound = 0 Then n\Sound = LoadSound("SFX\Slash1.ogg")
							If n\Sound2 = 0 Then n\Sound2 = LoadSound("SFX\Slash2.ogg")
							
							If dist>1.1 And KillTimer => 0 Then 
								n\CurrSpeed = CurveValue(n\Speed*0.8, n\CurrSpeed, 10.0)
								
								Animate2(n\obj, AnimTime(n\obj), 298, 316, n\CurrSpeed*10)
								
								If (prevframe < 307 And AnimTime(n\obj)=>307) Then
									PlaySound2(Step2SFX(Rand(3,5)), Camera, n\Collider, 10.0)
								EndIf
							Else
								;461, 476
								
								DeathMSG = ""
								
								n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
								
								Animate2(n\obj, AnimTime(n\obj), 451,493, 0.5, False)
								If (prevframe < 461 And AnimTime(n\obj)=>461) Then 
									If KillTimer => 0 Then Kill() : KillAnim = 0
									PlaySound(n\Sound)
								EndIf
								If (prevframe < 476 And AnimTime(n\obj)=>476) Then PlaySound(n\Sound2)
								If (prevframe < 486 And AnimTime(n\obj)=>486) Then PlaySound(n\Sound2)
								
							EndIf
							
							MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
					End Select
					
					If n\State <> 0 Then
						RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True	
						
						PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.1, EntityZ(n\Collider))
						RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True
						
						If dist > 8.0 Then
							ShowEntity n\obj2
							EntityAlpha n\obj2, Min(dist-8.0,1.0)
							
							PositionEntity(n\obj2, EntityX(n\obj), EntityY(n\obj) , EntityZ(n\obj))
							RotateEntity(n\obj2, 0, EntityYaw(n\Collider) - 180, 0)
							MoveEntity(n\obj2, 0, 30.0*0.025, -33.0*0.025)
							
							;render distance is set to 8.5 inside the forest,
							;so we need to cheat a bit to make the eyes visible if they're further than that
							pvt = CreatePivot()
							PositionEntity pvt, EntityX(Camera),EntityY(Camera),EntityZ(Camera)
							PointEntity pvt, n\obj2
							MoveEntity pvt, 0,0,8.0
							PositionEntity n\obj2, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
							FreeEntity pvt
						Else
							HideEntity n\obj2
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case NPCtype939
				;[Block]
				If n\State < 66 Then 
					Select n\State
						Case 0
							Animate2(n\obj,AnimTime(n\obj),290,405,0.1)
						Case 1
							
							If AnimTime(n\obj)=>644 And AnimTime(n\obj)<683 Then ;finish the walking animation
								n\CurrSpeed = CurveValue(n\Speed*0.2, n\CurrSpeed, 10.0)
								Animate2(n\obj,AnimTime(n\obj),644,683,28*n\CurrSpeed,False)
								If AnimTime(n\obj)=683 Then SetAnimTime(n\obj,175)
							Else
								n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
								Animate2(n\obj,AnimTime(n\obj),175,297,0.22,False)
								If AnimTime(n\obj)=297 Then n\State = 2
							EndIf
							
							n\LastSeen = 0
							
							MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor						
							
						Case 2
							n\State2 = Max(n\State2, 4)
							
							dist = EntityDistance(n\Collider, PlayerRoom\Objects[n\State2])
							
							n\CurrSpeed = CurveValue(n\Speed*0.3*Min(dist,1.0), n\CurrSpeed, 10.0)
							MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor 
							
							prevframe = AnimTime(n\obj)
							Animate2(n\obj,AnimTime(n\obj),644,683,28*n\CurrSpeed) ;walk
							
							If (prevframe<664 And AnimTime(n\obj)=>664) Or (prevframe>673 And AnimTime(n\obj)<654) Then
								PlaySound2(StepSFX(1, 0, Rand(0,3)), Camera, n\Collider, 12.0)
								If Rand(10)=1 Then
									temp = False
									If n\SoundChn = 0 Then 
										temp = True
									ElseIf Not ChannelPlaying(n\SoundChn)
										temp = True
									EndIf
									If temp Then
										If n\Sound <> 0 Then FreeSound n\Sound : n\Sound = 0
										n\Sound = loadsound_strict("SFX\939\"+(n\ID Mod 2)+"Lure"+Rand(1,10)+".ogg")
										n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)
									EndIf
								EndIf
							EndIf
							
							PointEntity n\obj, PlayerRoom\Objects[n\State2]
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),20.0), 0
							
							If dist<0.4 Then
								n\State2 = n\State2 + 1
								If n\State2 > 7 Then n\State2 = 4
								n\State = 1
							EndIf
							
						Case 3
							If EntityVisible(Collider, n\Collider) Then
								If n\Sound2 = 0 Then n\Sound2 = LoadSound("SFX\Slash1.ogg")
								
								n\EnemyX = EntityX(Collider)
								n\EnemyZ = EntityZ(Collider)
								n\LastSeen = 10*7
							EndIf
							
							If n\LastSeen > 0 Then
								prevframe = AnimTime(n\obj)
								
								If Distance(n\EnemyX, n\EnemyZ, EntityX(n\Collider), EntityZ(n\Collider))>1.1 Then
									n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
									
									Animate2(n\obj,AnimTime(n\obj),449,464,6*n\CurrSpeed) ;run
									
									If (prevframe<452 And AnimTime(n\obj)=>452) Or (prevframe<459 And AnimTime(n\obj)=>459) Then
										PlaySound2(StepSFX(1, 1, Rand(0,3)), Camera, n\Collider, 12.0)
									EndIf
									
									pvt = CreatePivot()
									PositionEntity pvt, n\EnemyX, 0, n\EnemyZ
									
									PointEntity n\obj, pvt
									RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),15.0), 0		
									
									FreeEntity pvt
								Else
									If n\LastSeen = 10*7 Then ;player is visible
										n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
										Animate2(n\obj,AnimTime(n\obj),18,68,0.5,True)
										
										If prevframe < 24 And AnimTime(n\obj)>=24 Then
											PlaySound n\Sound2
											Injuries = Injuries + Rnd(1.5, 2.5)-WearingVest*0.5
											BlurTimer = 500
										ElseIf prevframe < 57 And AnimTime(n\obj)>=57
											PlaySound DeathSFX(2)	
											Injuries = Injuries + Rnd(1.5, 2.5)-WearingVest*0.5
											BlurTimer = 500
										EndIf
										
										If Injuries>4.0 Then 
											DeathMSG="All four escaped SCP-939 specimens have been captured and recontained successfully. "
											DeathMSG=DeathMSG+"Two of them made quite a mess at Storage Area 6. A cleaning team has been dispatched."
											Kill()
											If (Not GodMode) Then n\State = 5
										EndIf
									Else ;player isn't visible, idle 
										n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
										Animate2(n\obj,AnimTime(n\obj),175,297,5*n\CurrSpeed,True)
									EndIf
								EndIf
								
								MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor							
								
								n\LastSeen = n\LastSeen - FPSfactor
							Else
								n\State = 2
							EndIf
							
						;Animate2(n\obj,AnimTime(n\obj),406,437,0.1) ;leap
						Case 5
							If AnimTime(n\obj)<68 Then
								Animate2(n\obj,AnimTime(n\obj),18,68,0.5,False) ;finish the attack animation
							Else
								Animate2(n\obj,AnimTime(n\obj),464,473,0.5,False) ;attack to idle
							EndIf
							
					End Select
					
					If n\State < 3 Then
						dist = EntityDistance(n\Collider, Collider)
						
						If dist < 4.0 Then dist = dist - EntityVisible(Collider, n\Collider)
						If PlayerSoundVolume*1.2>dist Or dist < 1.5 Then
							If n\State3 = 0 Then
								If n\Sound <> 0 Then FreeSound n\Sound : n\Sound = 0
								n\Sound = loadsound_strict("SFX\939\"+(n\ID Mod 2)+"Attack"+Rand(1,3)+".ogg")
								n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)										
								
								PlaySound(LoadTempSound("SFX\939\attack.ogg"))
								n\State3 = 1
							EndIf
							
							n\State = 3
						ElseIf PlayerSoundVolume*1.6>dist
							If n\State<>1 Then
								If n\Sound <> 0 Then FreeSound n\Sound : n\Sound = 0
								n\Sound = loadsound_strict("SFX\939\"+(n\ID Mod 2)+"Alert"+Rand(1,3)+".ogg")
								n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)	
								
								SetAnimTime(n\obj, 175)	
							EndIf
							
							n\State = 1
							
						EndIf
					EndIf				
					
					RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True	
					
					PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.28, EntityZ(n\Collider))
					RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True					
				EndIf
				;[End Block]
			Case NPCtype066
				;[Block]
				
				Select n\State
					Case 0 
						dist = Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))
						;idle: moves around randomly from waypoint to another if the player is far enough
						;starts staring at the player when the player is close enough
						
						If dist > 20.0 Then
							Animate2(n\obj, AnimTime(n\obj), 451, 612, 0.2, True)
							
							If n\State2 < MilliSecs() Then
								For w.waypoints = Each WayPoints
									If w\door = Null Then
										If Abs(EntityX(w\obj,True)-EntityX(n\Collider))<4.0 Then
											If Abs(EntityZ(w\obj,True)-EntityZ(n\Collider))<4.0 Then
												PositionEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.3, EntityZ(w\obj,True)
												ResetEntity n\Collider
												Exit
											EndIf
										EndIf
									EndIf
								Next
								n\State2 = MilliSecs()+5000
							EndIf
						ElseIf dist < 8.0
							n\LastDist = Rnd(1.0, 2.5)
							n\State = 1
						EndIf
					Case 1 ;staring at the player
						
						If AnimTime(n\obj)<451 Then
							angle = WrapAngle(CurveAngle(DeltaYaw(n\Collider, Collider)-180, (AnimTime(n\obj)-2.0)/1.2445, 15.0))
							;0->360 = 2->450
							SetAnimTime(n\obj, angle*1.2445+2.0)							
						Else
							Animate2(n\obj, AnimTime(n\obj), 636, 646, 0.4, False)
							If AnimTime(n\obj)=646 Then SetAnimTime (n\obj, 2)
						EndIf
						DebugLog -DeltaYaw(n\Collider, Collider)
						
						dist = Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))
						
						If Rand(700)=1 Then PlaySound2(LoadTempSound("SFX\066\eric.ogg"),Camera, n\Collider, 8.0)
						
						If dist < 1.0+n\LastDist Then n\State = Rand(2,3)
					Case 2 ;roll towards the player and make a sound, and then escape	
						If AnimTime(n\obj) < 647 Then 
							angle = CurveAngle(0, (AnimTime(n\obj)-2.0)/1.2445, 5.0)
							SetAnimTime(n\obj, angle*1.2445+2.0)
							If angle < 5 Or angle > 355 Then SetAnimTime(n\obj, 647)
						Else
							If AnimTime(n\obj)=683 Then 
								If n\State2 = 0 Then
									If Rand(2)=1 Then
										PlaySound2(LoadTempSound("SFX\066\eric.ogg"),Camera, n\Collider, 8.0)
									Else
										PlaySound2(LoadTempSound("SFX\066\notes"+Rand(1,4)+".ogg"),Camera, n\Collider, 8.0)
									EndIf									
									
									Select Rand(1,6)
										Case 1
											If n\Sound=0 Then n\Sound=LoadSound_strict("SFX\066\beethoven.ogg")
											n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)
											CameraShake = 10.0											
										Case 2
											n\State3 = Rand(700,1400)
										Case 3
											For d.Doors = Each Doors
												If d\locked = False And d\KeyCard = 0 And d\Code = "" Then
													If Abs(EntityX(d\frameobj)-EntityX(n\Collider))<16.0 Then
														If Abs(EntityZ(d\frameobj)-EntityZ(n\Collider))<16.0 Then
															UseDoor(d, False)
														EndIf
													EndIf
												EndIf
											Next
										Case 4
											If PlayerRoom\RoomTemplate\disabledecals = False Then
												CameraShake = 5.0
												de.Decals = CreateDecal(1, EntityX(n\Collider), 0.01, EntityZ(n\Collider), 90, Rand(360), 0)
												de\Size = 0.3 : UpdateDecals
												PlaySound(LoadTempSound("SFX\bodyfall.ogg"))
												If Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))<0.8 Then
													Injuries = Injuries + Rnd(0.3,0.5)
												EndIf
											EndIf
									End Select
								EndIf
								
								n\State2 = n\State2+FPSfactor
								If n\State2>70 Then 
									n\State = 3
									n\State2 = 0
								EndIf
							Else
								n\CurrSpeed = CurveValue(n\Speed*1.5, n\CurrSpeed, 10.0)
								PointEntity n\obj, Collider
								;angle = CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10);1.0/Max(n\CurrSpeed,0.0001))
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj)-180, EntityYaw(n\Collider), 10), 0
								
								Animate2(n\obj, AnimTime(n\obj), 647, 683, n\CurrSpeed*25, False)
								
								MoveEntity n\Collider, 0,0,-n\CurrSpeed*FPSfactor
								
							EndIf
						EndIf
					Case 3
						PointEntity n\obj, Collider
						angle = CurveAngle(EntityYaw(n\obj)+n\Angle-180, EntityYaw(n\Collider), 10);1.0/Max(n\CurrSpeed,0.0001))
						RotateEntity n\Collider, 0, angle, 0
						
						n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 10.0)
						MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
						
						;Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\CurrSpeed*25)
						
						If Rand(100)=1 Then n\Angle = Rnd(-20,20)
						
						n\State2 = n\State2 + FPSfactor
						If n\State2>250 Then 
							Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\CurrSpeed*25, False)
							If AnimTime(n\obj)=647 Then 
								n\State = 0
								n\State2=0
							EndIf
						Else
							Animate2(n\obj, AnimTime(n\obj), 684, 647, -n\CurrSpeed*25)
						EndIf
						
				End Select
				
				If n\SoundChn<>0 Then
					If ChannelPlaying(n\SoundChn) Then
						n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 20)
						dist = Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))
						BlurTimer = Max((5.0-dist)*300,0)
					EndIf
				EndIf
				
				If n\State3 > 0 Then
					n\State3 = n\State3-FPSfactor
					LightVolume = TempLightVolume-TempLightVolume*Min(Max(n\State3/500,0.01),0.6)
					HeartBeatRate = Max(HeartBeatRate, 130)
					HeartBeatVolume = Max(HeartBeatVolume,Min(n\State3/1000,1.0))
				EndIf
				
				;DebugLog n\State+" - "+n\State2 + " - "+AnimTime(n\obj)+" - "+(EntityYaw(Collider)-DeltaYaw( Collider, Collider))
				
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
				
				RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), 0
				;[End Block]
		End Select
		
		If Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))<HideDistance*0.7 Then 
			TranslateEntity n\Collider, 0, n\DropSpeed, 0
			
			Local CollidedFloor% = False
			For i% = 1 To CountCollisions(n\Collider)
				If CollisionY(n\Collider, i) < EntityY(n\Collider) - 0.01 Then CollidedFloor = True : Exit
			Next
			
			If CollidedFloor = True Then
				n\DropSpeed# = 0
			Else
				n\DropSpeed# = Max(n\DropSpeed - 0.005*FPSfactor,-0.2)
			EndIf
		Else
			n\DropSpeed = 0
		EndIf
		
	Next
	
End Function


Function Shoot(x#,y#,z#,hitProb#=1.0,particles%=True)
	
	;muzzle flash
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	LightVolume = TempLightVolume*1.2
	
	If (Not GodMode) Then 
		
		If Rnd(1.0)=<hitProb Then
			TurnEntity Camera, Rnd(-3,3), Rnd(-3,3), 0
			
			If WearingVest>0 Then
				If WearingVest = 1 Then
					Select Rand(8)
						Case 1,2,3,4,5
							BlurTimer = 500
							Stamina = 0
							Msg = "Air escapes from your lungs as something hits your vest" : MsgTimer = 70*6		
							Injuries = Injuries + Rnd(0.1,0.5)
						Case 6
							BlurTimer = 500
							Msg = "You feel a burning pain in your left leg" : MsgTimer = 70*6
							Injuries = Injuries + Rnd(0.8,1.2)
						Case 7
							BlurTimer = 500
							Msg = "You feel a burning pain in your right leg" : MsgTimer = 70*6		
							Injuries = Injuries + Rnd(0.8,1.2)
						Case 8
							Kill()
					End Select	
				Else
					If Rand(10)=1 Then
						Kill()
					Else
						Msg = "You feel something hitting your vest" : MsgTimer = 70*6	
						Injuries = Injuries + Rnd(0.1,0.5)
					EndIf
				EndIf
			Else
				Select Rand(6)
					Case 1
						Kill()
					Case 2
						BlurTimer = 500
						Msg = "You feel a burning pain in your left leg" : MsgTimer = 70*6
						Injuries = Injuries + Rnd(0.8,1.2)
					Case 3
						BlurTimer = 500
						Msg = "You feel a burning pain in your right leg" : MsgTimer = 70*6	
						Injuries = Injuries + Rnd(0.8,1.2)
					Case 4
						BlurTimer = 500
						Msg = "You feel a burning pain in your right shoulder" : MsgTimer = 70*6			
						Injuries = Injuries + Rnd(0.8,1.2)	
					Case 5
						BlurTimer = 500
						Msg = "You feel a burning pain in your left shoulder" : MsgTimer = 70*6			
						Injuries = Injuries + Rnd(0.8,1.2)	
					Case 6
						BlurTimer = 500
						Msg = "You feel a burning pain in your abdomen" : MsgTimer = 70*6
						Injuries = Injuries + Rnd(2.5,4.0)
				End Select
			EndIf
			
			Injuries = Min(Injuries, 4.0)
			
			;Kill()
			PlaySound BullethitSFX
		ElseIf particles
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
			PointEntity pvt, p\obj
			TurnEntity pvt, 0, 180, 0
			
			EntityPick(pvt, 2.5)
			
			FreeEntity pvt
			
			If PickedEntity() <> 0 Then 
				PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
				
				If particles Then 
					;dust/smoke particles
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
					p\speed = 0.001
					p\SizeChange = 0.003
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
					
					For i = 0 To Rand(2,3)
						p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
						p\speed = 0.02
						p\A = 0.8
						p\Achange = -0.01
						RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
					Next
					
					;bullet hole decal
					Local de.Decals = CreateDecal(Rand(13,14), PickedX(),PickedY(),PickedZ(), 0,0,0)
					AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
					MoveEntity de\obj, 0,0,-0.001
					EntityFX de\obj, 1
					de\lifetime = 70*20
					EntityBlend de\obj, 2
					de\Size = Rnd(0.028,0.034)
					ScaleSprite de\obj, de\Size, de\Size
				EndIf				
			EndIf
			
		EndIf
		
	EndIf
	
End Function

Function PlayMTFSound(sound%, n.NPCs)
	If n <> Null Then
		n\SoundChn = PlaySound2(sound, Camera, n\Collider, 8.0)	
	EndIf
	
	
	If SelectedItem <> Null Then
		If SelectedItem\state2 = 3 And SelectedItem\state > 0 Then 
			Select SelectedItem\itemtemplate\tempname 
				Case "radio","fineradio","18vradio"
					If RadioCHN(3)<> 0 Then StopChannel RadioCHN(3)
					RadioCHN(3) = PlaySound (sound)
			End Select
		EndIf
	EndIf 
End Function


Function Find860Angle(n.NPCs, fr.Forest)
	TFormPoint(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,PlayerRoom\obj)
	Local playerx = Floor((TFormedX()*RoomScale+6.0)/12.0)
	Local playerz = Floor((TFormedZ()*RoomScale+6.0)/12.0)
	
	TFormPoint(EntityX(n\Collider),EntityY(n\Collider),EntityZ(n\Collider),0,PlayerRoom\obj)
	Local x# = (TFormedX()*RoomScale+6.0)/12.0
	Local z# = (TFormedZ()*RoomScale+6.0)/12.0
	
	Local xt = Floor(x), zt = Floor(z)
	
	Local x2,z2
	If xt<>playerx Or zt<>playerz Then ;the monster is not on the same tile as the player
		For x2 = Max(xt-1,0) To Min(xt+1,gridsize-1)
			For z2 = Max(zt-1,0) To Min(zt+1,gridsize-1)
				If fr\grid[(z2*gridsize)+x2]>0 And (x2<>xt Or z2<>zt) And (x2=xt Or z2=zt) Then
					
					;tile (x2,z2) is closer to the player than the monsters current tile
					If (Abs(playerx-x2)+Abs(playerz-z2))<(Abs(playerx-xt)+Abs(playerz-zt)) Then
						Return point_direction(x-0.5,z-0.5,x2,z2)+EntityYaw(PlayerRoom\obj)+180
					EndIf
					
				EndIf
			Next
		Next
	Else
		Return point_direction(EntityX(n\Collider),EntityZ(n\Collider),EntityX(Collider),EntityZ(Collider))+180
	EndIf		
End Function

;~IDEal Editor Parameters:
;~F#7#23#196#1BA#2B6#39C#50F#5D0#659#6EC#941#965#9FA#A34#AC1#C3E#CE4#D95#E0D#E1F
;~C#Blitz3D