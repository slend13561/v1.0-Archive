
Graphics 1024,768,0,2
SetBuffer BackBuffer()

AppTitle "SCP-CB Map Creator"

Include "StrictLoads.bb"

Const ROOM1% = 1, ROOM2% = 2, ROOM2C% = 3, ROOM3% = 4, ROOM4% = 5


Global RoomTempID%
Type RoomTemplates
	Field Shape%, Name$
	Field Description$
	Field Large%
	Field id
	
	Field events$[5]
End Type 	

Function CreateRoomTemplate.RoomTemplates(meshpath$)
	Local rt.RoomTemplates = New RoomTemplates
	
	rt\id = RoomTempID
	RoomTempID=RoomTempID+1
	
	Return rt
End Function

Function LoadRoomTemplates(file$)
	Local TemporaryString$, Temp%, i%, n%
	Local rt.RoomTemplates = Null
	Local StrTemp$ = ""
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			If TemporaryString <> "room ambience" Then
				rt = CreateRoomTemplate(StrTemp)
				rt\Name = TemporaryString
				
				StrTemp = Lower(GetINIString(file, TemporaryString, "shape"))
				Select StrTemp
					Case "room1", "1"
						rt\Shape = ROOM1
					Case "room2", "2"
						rt\Shape = ROOM2
					Case "room2c", "2c", "2c"
						rt\Shape = ROOM2C
					Case "room3", "3"
						rt\Shape = ROOM3
					Case "room4", "4"
						rt\Shape = ROOM4
					Default
				End Select
				
				rt\Description = GetINIString(file, TemporaryString, "descr")
				rt\Large = GetINIInt(file, TemporaryString, "large")
				
			EndIf
			
		EndIf
	Wend
	
	CloseFile f
	
End Function

Function InitEvents()
	
	For rt.roomtemplates = Each RoomTemplates
		Select rt\name
			Case "173"
				rt\events[0]="173"
			Case "008"
				rt\events[0]="008"
			Case "914"
				rt\events[0]="914"
			Case "coffin"
				rt\events[0]="coffin"
				rt\events[1]="coffin106"
			Case "gatea"
				rt\events[0]="gatea"	
			Case "gateaentrance"
				rt\events[0]="gateaentrance"	
			Case "exit1"
				rt\events[0]="exit1"	
			Case "endroom"
				rt\events[0]="endroom106"	
			Case "start"
				rt\events[0]="alarm"	
			Case "pocketdimension"
				rt\events[0]="pocketdimension"
			Case "checkpoint1","checkpoint2"
				rt\events[0]="checkpoint"
			Case "lockroom"
				rt\events[0]="lockroom173"
			Case "lockroom2"
				rt\events[0]="lockroom096"	
			Case "testroom"
				rt\events[0]="testroom"
			Case "tunnel2"
				rt\events[0]="tunnel2"
				rt\events[1]="tunnel2smoke"
			Case "roompj"
				rt\events[0]="pj"
			Case "room012"
				rt\events[0]="room012"
			Case "room035"
				rt\events[0]="room035"
			Case "room049"
				rt\events[0]="room049"
			Case "room079"
				rt\events[0]="room079"
			Case "room106"
				rt\events[0]="room106"
			Case "room2"
				rt\events[0]="room2trick"
			Case "room2_2"
				rt\events[0]="room2fan"			
			Case "room2cafeteria"
				rt\events[0]="room2cafeteria"						
			Case "room2ccont"
				rt\events[0]= "room2ccont"
			Case "room2closets"
				rt\events[0]="room2closets"
			Case "room2doors"
				rt\events[0]="room2doors173"
			Case "room2elevator"
				rt\events[0]="room2elevator"
			Case "room2nuke"
				rt\events[0]="room2nuke"
			Case "room2offices2"
				rt\events[0]="room2offices2"
			Case "room2offices3"
				rt\events[0]="room2offices3"
			Case "room2pipes"
				rt\events[0]="room2pipes106"	
			Case "room2pit"
				rt\events[0]="room2pit"	
			Case "room2poffices2"
				rt\events[0]="room2poffices2"					
			Case "room2servers"
				rt\events[0]="room2servers"
			Case "room2storage"
				rt\events[0]="room2storage"
			Case "room2tesla"
				rt\events[0]="room2tesla"	
			Case "room2testroom2"
				rt\events[0]="testroom173"	
			Case "room2toilets"
				rt\events[0]="toiletguard"	
				rt\events[1]="buttghost"	
			Case "room2tunnel"
				rt\events[0]="room2tunnel"	
			Case "room3", "room3tunnel"
				rt\events[0]="room3door"				
			Case "room3servers","room3servers2"
				rt\events[0]="room3servers"
			Case "room3storage"
				rt\events[0]="room3storage"
			Case "room3pit"
				rt\events[0]="room3pit"
			Case "room3tunnel"
				rt\events[0]="room3tunnel"
			Case "room3","room3_2" 
				rt\events[0]="106victim"
			Case "room4"
				rt\events[0]="room4"
			Case "room860"
				rt\events[0]="room860"
			Case "tunnel"
				rt\events[0]="tunnel106"				
		End Select
	Next
	
End Function

ChangeDir ".."

Global Font1 = LoadFont_Strict("GFX\cour.ttf", 16)
Global ButtonSFX% = LoadSound_Strict("SFX\button.ogg")

ChangeDir "Map Creator"
SetFont Font1

Dim MapIcons(5,4)
MapIcons(ROOM1, 0)=LoadImage_Strict("room1.png")
MapIcons(ROOM2, 0)=LoadImage_Strict("room2.png")
MapIcons(ROOM2C, 0)=LoadImage_Strict("room2C.png")
MapIcons(ROOM3, 0)=LoadImage_Strict("room3.png")
MapIcons(ROOM4, 0)=LoadImage_Strict("room4.png")

For i = ROOM1 To ROOM4
	MaskImage MapIcons(i,0), 255,255,255
	HandleImage MapIcons(i,0),8,8
	For n = 1 To 3
		MapIcons(i,n)=CopyImage(MapIcons(i,0))
		MaskImage MapIcons(i,n), 255,255,255
		RotateImage(MapIcons(i,n),90*n)
		If n = 2 
			HandleImage MapIcons(i,n),9,9
		Else
			HandleImage MapIcons(i,n),8,8
		EndIf
	Next
Next

Dim Arrows(4)
Arrows(0) = LoadImage_Strict("arrows.png")
HandleImage Arrows(0),ImageWidth(Arrows(0))/2,ImageHeight(Arrows(0))/2
For i = 1 To 3
	Arrows(i)=CopyImage(Arrows(0))
	HandleImage Arrows(i), ImageWidth(Arrows(0))/2,ImageHeight(Arrows(0))/2
	RotateImage Arrows(i), i*90
Next

Const ClrR = 50, ClrG = 50, ClrB = 50

Dim SavedMaps$(30)
Global CurrMap$, SavePath$, Saved%

LoadSavedMaps()

Global FileLocation$ = "..\Data\rooms.ini"

LoadRoomTemplates(FileLocation)
Global SelectedRoomTemplate.RoomTemplates
Global SelectedX%, SelectedY%, RotateRoom%

InitEvents()

Global MapWidth% = GetINIInt("..\options.ini", "options", "map size"), MapHeight% = GetINIInt("..\options.ini", "options", "map size")
Dim Map.RoomTemplates(MapWidth, MapHeight)
Dim MapAngle%(MapWidth, MapHeight)
Dim MapEvent$(MapWidth, MapHeight)
Dim MapEventProb#(MapWidth, MapHeight)
;Dim MapAngle%(MapWidth, MapHeight)

Global RoomTemplateAmount = 0

For rt.RoomTemplates = Each RoomTemplates
	If rt\Name = "start" Then
		Map(MapWidth/2-1,MapHeight)=rt
		MapEvent(MapWidth/2-1,MapHeight)="alarm"
		MapAngle(MapWidth/2-1,MapHeight)=180
	EndIf
	RoomTemplateAmount = RoomTemplateAmount + 1
Next

Global MouseDown1%, MouseHit1%, MouseDown2%, MouseSpeedX#, MouseSpeedY#
Global SelectedTextBox% = 0
Global TickIMG = LoadImage_Strict("tickimg.png")

strtemp$ = "aaaa"

Repeat
	
	Local x,y,width,height
	Local ScrollMenuY#, ScrollMenuHeight#
	
	MouseDown1 = MouseDown(1)
	MouseHit1 = MouseHit(1)
	MouseDown2 = MouseDown(2)
	MouseSpeedX# = MouseXSpeed()
	MouseSpeedY# = MouseYSpeed()
	
	
	Cls
	ClsColor ClrR, ClrG, ClrB
	
	x = 20
	y = 50
	width = 180
	height = 768-70
	TextBox (x,y,width,height,"")
	
	y = y-(ScrollMenuY#*height)*0.5
	
	For rt.RoomTemplates = Each RoomTemplates
		If y > 40 And y < 30+height Then 
			If SelectedRoomTemplate = rt Then 
				Color 170, 170, 170
				Rect x+2,y+10,width-4,16
				Color 0,0,0
			EndIf			
			
			If MouseY()>y+9 And MouseY()<y+9+18 Then
				If MouseX()>x+1 And MouseX()<x+(width-1) Then
					Rect x+1,y+9,width-3,18,False
					If MouseHit1 Then
						PlaySound ButtonSFX
						If SelectedRoomTemplate = rt Then
							SelectedRoomTemplate = Null
						Else
							SelectedRoomTemplate = rt
							SelectedX=0
							SelectedY=0
						EndIf
					EndIf
				EndIf
			EndIf
			
			Text x+10, y+10, rt\Name
		EndIf
		y=y+18
	Next
	
	x = x+width+1
	y = 50
	width = 20
	
	ScrollMenuY# = DrawScrollBar(x,y,width,height,x, y + ((height - ScrollMenuHeight) * ScrollMenuY),20,ScrollMenuHeight,ScrollMenuY,1)
	
	ScrollMenuHeight = height * ((height / 15) / Max(RoomTemplateAmount, height / 15)) ;' palkin korkeus	 
	
	x = 20+240
	y = 50
	width = 500
	height = 500
	TextBox (x,y,width,height,"")
	
	If Saved Then 
		Text x+width/2, y+15, CurrMap, True
	ElseIf CurrMap<>""
		Text x+width/2, y+15, CurrMap+"*", True
	EndIf
	
	
	If CurrMap <> "" Then 
		If Button (x+width/2-75, y+height-30, 150, 20, "Delete map", False) Then
			DebugLog CurrentDir()+"Maps\"+SavePath+".cbmap"
			If FileType(CurrentDir()+"Maps\"+SavePath+".cbmap")=1 Then 
				DeleteFile(CurrentDir()+"Maps\"+SavePath+".cbmap")
				SavePath = ""
				CurrMap = ""
				EraseMap()
				LoadSavedMaps()
			EndIf
		EndIf
	EndIf
	
	x = x+(width/2)-(MapWidth*10)-10
	y = y+(height/2)-(MapHeight*10)-10
	
	Local mx%,my%,temp%,nx%,ny%
	
	For mx = 0 To MapWidth
		For my = 0 To MapHeight
			If SelectedX> 0 Then 
				If mx=SelectedX And my=SelectedY Then
					Color 200,200,200
					Rect(x + mx * 20+1, y + my * 20+1, 17, 17)
				EndIf
			EndIf			
			
			If Map(mx, my) = Null Then
				If mx=0 Or mx=MapWidth Or my=0 Or my = MapHeight Then
					Color 220, 220, 220
				Else
					Color 170, 170, 170
				EndIf
				
				Rect(x + mx * 20, y + my * 20, 19, 19,False)
			Else
				Color 0,0,0
				DrawImage(MapIcons(Map(mx,my)\Shape, Floor(MapAngle(mx,my)/90.0)), x + mx * 20 + 9, y + my * 20 + 9)
				Color 100,100,100
				If Map(mx,my)\Large Then Rect (x + mx * 20 - 9, y + my * 20 - 9, 38, 38,False)
				;Rect(x + mx * 20, y + my * 20, 19, 19,False)
			End If
			
			If MouseX()>x + mx * 20 And mx>0 And mx<MapWidth Then
				If MouseX()<x + mx * 20+19 Then
					If MouseY()>y + my * 20 And my > 0 And my < MapHeight Then
						If MouseY()<y + my * 20+19 Then
							Color 0,0,0
							Rect(x + mx * 20+1, y + my * 20+1, 17, 17,False)
							
							If MouseHit1 Then
								If SelectedRoomTemplate <> Null Then
									temp = True
									;don't allow placing two large rooms next to each it other
									If SelectedRoomTemplate\Large Then 
										For nx = Max(mx-1,1) To Min(mx+1,MapWidth)
											For ny = Max(my-1,1) To Min(my+1,MapHeight)
												If Map(nx,ny)<>Null Then
													If Map(nx,ny)\Large Then temp = False : Exit
												EndIf
											Next
										Next
									EndIf
									
									If temp Then 
										Map(mx,my)=SelectedRoomTemplate
										MapEventProb(mx,my)=1.0
										Saved = False										
									EndIf
									
									
								Else
									SelectedRoomTemplate = Null
									If Map(mx,my)<>Null Then 
										SelectedX = mx
										SelectedY = my
										RotateRoom = True
									Else
										SelectedX=0
										SelectedY=0
									EndIf
								EndIf
							ElseIf MouseDown2
								Map(mx,my)=Null
								Saved = False
								If SelectedX = mx And SelectedY=my Then 
									SelectedX=0
									SelectedY=0
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		Next
	Next
	
	If MouseDown1 And Map(SelectedX, SelectedY)<>Null Then
		If RotateRoom Then 
			DrawImage Arrows(Floor(MapAngle(SelectedX, SelectedY)/90)), x + SelectedX*20 + 10, y + SelectedY*20 + 10
			
			If Distance(x + SelectedX*20 + 10, y + SelectedY*20 + 10, MouseX(), MouseY())>15 Then
				Saved = False
				MapAngle(SelectedX, SelectedY) = WrapAngle(Floor((GetAngle(x + SelectedX*20 + 10, y + SelectedY*20 + 10, MouseX(), MouseY())+45.0)/90.0)*90.0-90)
			EndIf
		EndIf
	Else
		RotateRoom = False
	EndIf
	
	
	x = 20+240
	y = 570
	width = 500
	height = 768-590
	TextBox (x,y,width,height,"")
	
	If SelectedRoomTemplate <> Null Then
		Text x+20, y+20, "Selected room template: "+SelectedRoomTemplate\Name
		Text x+20, y+50, SelectedRoomTemplate\Description
		
		If Button(x+20, y+80, 150,25, "Deselect") Then SelectedRoomTemplate = Null
		
	ElseIf SelectedX <> 0
		Text x+20, y+20, "Selected room: " +Map(SelectedX,SelectedY)\Name
		Text x+20, y+50, Map(SelectedX,SelectedY)\Description
		
		Text x+20, y+80, "Angle: "
		If Button(x+80, y+80-4, 20,20, "-") Then MapAngle(SelectedX,SelectedY)=WrapAngle(MapAngle(SelectedX,SelectedY)-90)
		Text x+130, y+80, MapAngle(SelectedX,SelectedY), True
		If Button(x+160, y+80-4, 20,20, "+") Then MapAngle(SelectedX,SelectedY)=WrapAngle(MapAngle(SelectedX,SelectedY)+90)
		
		Text x+20, y+110, "Events: "
		y=y+110+20
		For i = 0 To 4
			If Map(SelectedX,SelectedY)\events[i]<>"" Then
				Text x+50, y, Map(SelectedX,SelectedY)\events[i]
				If Tick(x+20, y, (MapEvent(SelectedX,SelectedY)=Map(SelectedX,SelectedY)\events[i])) Then
					MapEvent(SelectedX,SelectedY)=Map(SelectedX,SelectedY)\events[i]
					If Button(x+240, y-4, 20,20, "-") Then 
						MapEventProb(SelectedX,SelectedY) = Max(MapEventProb(SelectedX,SelectedY)-0.1, 0.0)
						Saved = False
					EndIf
					Text x+335, y, "Probability: "+ MapEventProb(SelectedX,SelectedY), True
					If Button(x+410, y-4, 20,20, "+") Then 
						MapEventProb(SelectedX,SelectedY) = Min(MapEventProb(SelectedX,SelectedY)+0.1, 1.0)		
						Saved = False
					EndIf
				Else
					If MapEvent(SelectedX,SelectedY)=Map(SelectedX,SelectedY)\events[i] Then MapEvent(SelectedX,SelectedY)=""
				EndIf
				
				y=y+20
			EndIf
		Next
	EndIf
	
	x = x + width + 40
	y = 50
	width = 1024 - x - 20
	height = 500
	TextBox (x,y,width,height,"")
	
	
	Text x+20,y+20,"Saved maps: "
	y=y+40
	For i = 0 To 20
		If SavedMaps(i)<>"" Then
			If CurrMap = SavedMaps(i) Then 
				Color 170, 170, 170
				Rect x+2,y,width-4,20
				Color 0,0,0
				If Saved Then 
					Text x+20, y+2, SavedMaps(i)
				Else
					Text x+20, y+2, SavedMaps(i)+"*"	
				EndIf
			Else
				Text x+20, y+2, SavedMaps(i)
			EndIf
			
			
			If MouseX()>x And MouseX()<x+width Then
				If MouseY()> y And MouseY()<y+19 Then
					Rect x+1,y,width-3,20,False
					If MouseHit1 Then 
						PlaySound ButtonSFX
						SavePath = SavedMaps(i)
						CurrMap = SavePath
						LoadMap("Maps\"+SavedMaps(i))
					EndIf
				EndIf
			EndIf			
			y=y+20
		EndIf
	Next
	
	
	y = 50+550 - 20
	width = 1024 - x - 20
	height = 25
	
	If Button(x,y,width,height, "Save", (CurrMap="")) Then
		SaveMap("Maps\"+CurrMap)
	EndIf	
	
	y = y + 40
	
	If Button(x,y,width,height, "Save as "+SavePath+"", (SavePath="")) Then
		If CurrMap = "" Then CurrMap = SavePath
		SaveMap("Maps\"+SavePath)
		LoadSavedMaps()
	EndIf	
	
	SavePath$ = Left(InputBox(x,y+25,width,height,SavePath),15)
	
	If Button(x+width/2, 15, width/2, height, "QUIT") Then End
	
	
	Flip
	
	Delay 8
Forever

Function Button%(x,y,width,height,txt$, disabled%=False)
	Local Pushed = False
	
	Color ClrR, ClrG, ClrB
	If Not disabled Then 
		If MouseX() > x And MouseX() < x+width Then
			If MouseY() > y And MouseY() < y+height Then
				If MouseDown1 Then
					Pushed = True
					Color ClrR*0.6, ClrG*0.6, ClrB*0.6
				Else
					Color Min(ClrR*1.2,255),Min(ClrR*1.2,255),Min(ClrR*1.2,255)
				EndIf
			EndIf
		EndIf
	EndIf
	
	If Pushed Then 
		Rect x,y,width,height
		Color 133,130,125
		Rect x+1,y+1,width-1,height-1,False	
		Color 10,10,10
		Rect x,y,width,height,False
		Color 250,250,250
		Line x,y+height-1,x+width-1,y+height-1
		Line x+width-1,y,x+width-1,y+height-1
	Else
		Rect x,y,width,height
		Color 133,130,125
		Rect x,y,width-1,height-1,False	
		Color 250,250,250
		Rect x,y,width,height,False
		Color 10,10,10
		Line x,y+height-1,x+width-1,y+height-1
		Line x+width-1,y,x+width-1,y+height-1		
	EndIf
	
	Color 255,255,255
	If disabled Then Color 70,70,70
	Text x+width/2, y+height/2-1, txt, True, True
	
	Color 0,0,0
	
	If Pushed And MouseHit1 Then PlaySound ButtonSFX : Return True
End Function

Function Tick(x,y,selected%)
	TextBox(x,y,13,13,"")
	
	If selected Then
		DrawImage TickIMG, x, y
	EndIf
	
	If MouseX() > x And MouseX() < x+13 Then
		If MouseY() > y And MouseY() < y+13 Then
			If MouseHit1 Then PlaySound ButtonSFX : Return (Not selected)
		EndIf
	EndIf	
	
	Return selected
	
End Function

Function InputBox$(x,y,width,height,Txt$,ID=0)
	TextBox(x,y,width,height,Txt$)
	
	Local MouseOnBox = False
	
	If MouseX() > x And MouseX() < x+width Then
		If MouseY() > y And MouseY() < y+height Then
			MouseOnBox = True
			If MouseHit1 Then SelectedTextBox = ID : FlushKeys
		EndIf
	EndIf	
	
	If MouseOnBox = False And MouseHit1 And SelectedTextBox = ID Then SelectedTextBox = 0
	
	If SelectedTextBox = ID Then
		Txt = rInput(Txt)
		Color 0,0,0
		If (MilliSecs() Mod 800) < 400 Then  Rect x+width/2 + StringWidth(Txt)/2 + 2, y+height/2-5, 2, 12
	EndIf
	
	Return Txt
End Function

Function TextBox(x,y,width,height,Txt$)
	Color 255,255,255
	Rect x,y,width,height
	
	Color 128,128,128
	Rect x,y,width,height,False
	Color 64,64,64
	Rect x+1,y+1,width-2,height-2,False	
	Color 255,255,255
	Line x+width-1,y,x+width-1, y+height-1
	Line x, y+height-1, x+width-1, y+height-1	
	Color 212, 208, 199
	Line x+width-2,y+1,x+width-2, y+height-2
	Line x+1, y+height-2, x+width-2, y+height-2
	
	Color 0,0,0
	Text x+width/2, y+height/2, Txt, True, True
End Function

Function rInput$(aString$)
	value = GetKey()
	length = Len(aString$)
	If value = 8 Then value = 0 :If length > 0 Then aString$ = Left$(aString,Length-1)
	If value = 13 Then Goto ende
	If value = 0 Then Goto ende
	If value>0 And value<7 Or value>26 And value<32 Or value=9 Then Goto ende
	aString$=aString$ + Chr$(value)
	.ende
	Return aString$
End Function

Function SlideBar#(x, y, leveys, arvo)
	
	If MouseDown(1) Then
		If MouseX() >= x-5 And MouseX() <= x+leveys+15 And MouseY() >= y-5 And MouseY() <= y+5 Then
			arvo = Min(Max((MouseX()-x-5)*100/leveys, 0), 100)
		EndIf
	EndIf
	
	TextBox(x, y-5, leveys+10, 10, "")
	
	Button(x + leveys*arvo/100, y-5, 10, 11, "")
	
	Return arvo
	
End Function


Function DrawScrollBar#(x, y, width, height, barx, bary, barwidth, barheight, bar#, dir = 0)
	;0 = vaakasuuntainen, 1 = pystysuuntainen
	
	Color(0, 0, 0)
	Rect(x, y, width, height)
	Button(barx, bary, barwidth, barheight, "")
	
	If dir = 0 Then ;vaakasuunnassa
		If height > 10 Then
			Color 250,250,250
			Rect(barx + barwidth / 2, bary + 5, 2, barheight - 10)
			Rect(barx + barwidth / 2 - 3, bary + 5, 2, barheight - 10)
			Rect(barx + barwidth / 2 + 3, bary + 5, 2, barheight - 10)
		EndIf
	Else ;pystysuunnassa
		If width > 10 Then
			Color 250,250,250
			Rect(barx + 4, bary + barheight / 2, barwidth - 10, 2)
			Rect(barx + 4, bary + barheight / 2 - 3, barwidth - 10, 2)
			Rect(barx + 4, bary + barheight / 2 + 3, barwidth - 10, 2)
		EndIf
	EndIf
	
	If MouseDown1 Then
		If MouseX()>barx And MouseX()<barx+barwidth Then
			If MouseY()>bary And MouseY()<bary+barheight Then
				If dir = 0 Then
					Return Min(Max(bar + MouseSpeedX / Float(width - barwidth), 0), 1)
				Else
					Return Min(Max(bar + MouseSpeedY / Float(height - barheight), 0), 1)
				End If				
			EndIf
		EndIf
	End If
	
	Return bar

End Function



Function SaveMap(file$)
	f% = WriteFile(file+".cbmap")
	
	For x = 0 To MapWidth
		For y = 0 To MapHeight
			If Map(x,y)<>Null Then
				WriteByte f, x
				WriteByte f, y
				WriteString f, Lower(Map(x,y)\Name)
				WriteByte f, Floor(MapAngle(x,y)/90.0)
				WriteString f, MapEvent(x,y)
				WriteFloat f, MapEventProb(x,y)
			EndIf
		Next
	Next
	
	Saved = True
	
	CloseFile f
End Function

Function LoadMap(file$)
	EraseMap()
	
	f% = ReadFile(file+".cbmap")
	DebugLog file+".cbmap"
	
	While Not Eof(f)
		DebugLog "dsfkjmgndfklmgkl"
		x = ReadByte(f)
		y = ReadByte(f)
		name$ = ReadString(f)
		DebugLog x+", "+y+": "+name
		For rt.roomtemplates=Each RoomTemplates
			If Lower(rt\name) = name Then
				DebugLog rt\name
				Map(x,y)=rt
				Exit
			EndIf
		Next
		MapAngle(x,y)=ReadByte(f)*90
		MapEvent(x,y) = ReadString(f)
		MapEventProb(x,y) = ReadFloat(f)
	Wend
	
	Saved = True
	
	CloseFile f
End Function

Function LoadSavedMaps()
	
	For i = 0 To 20
		SavedMaps(i)=""
	Next
	
	myDir=ReadDir(CurrentDir()+"\Maps") 
	i = 0
	Repeat 
		file$=NextFile$(myDir)
		DebugLog file
		If file$="" Then Exit 
		If FileType("Maps\"+file$) = 1 Then 
			If file <> "." And file <> ".." Then 
				SavedMaps(i) = Left(file,Max(Len(file)-6,1))
				i=i+1
			EndIf
		End If 
	Forever 
	CloseDir myDir 
	
End Function


Function EraseMap()
	SelectedX = 0
	SelectedY = 0
	SelectedRoomTemplate = Null
	
	For x = 1 To MapWidth-1
		For y = 1 To MapHeight-1
			Map(x,y)=Null
			MapAngle(x,y)=0
			MapEvent(x,y)=""
			MapEventProb(x,y)=0.0
		Next
	Next
End Function

;INI-funktiot:
Function GetINIString$(file$, section$, parameter$)
	Local TemporaryString$ = ""
	Local f = ReadFile(file)
	
	While Not Eof(f)
		If ReadLine(f) = "["+section+"]" Then
			Repeat 
				TemporaryString = ReadLine(f)
				If Trim( Left(TemporaryString, Max(Instr(TemporaryString,"=")-1,0)) ) = parameter Then
					CloseFile f
					Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString,"=")) )
				EndIf
			Until Left(TemporaryString,1) = "[" Or Eof(f)
			CloseFile f
			Return ""
		EndIf
	Wend
	
	CloseFile f
End Function

Function GetINIInt%(file$, section$, parameter$)
	Local strtemp$ = Lower(GetINIString(file$, section$, parameter$))
	
	Select strtemp
		Case "true"
			Return 1
		Case "false"
			Return 0
		Default
			Return Int(strtemp)
	End Select
	Return 
End Function

Function GetINIFloat#(file$, section$, parameter$)
	Return GetINIString(file$, section$, parameter$)
End Function


; matemaattiset funktiot:
Function Min#(a#,b#)
	If a < b Then Return a Else Return b
End Function

Function Max#(a#,b#)
	If a > b Then Return a Else Return b
End Function

Function WrapAngle#(angle#)
	While angle < 0
		angle = angle + 360
	Wend 
	While angle >= 360
		angle = angle - 360
	Wend
	Return angle
End Function

Function GetAngle#(x1#, y1#, x2#, y2#)
	Return ATan2( y2 - y1, x2 - x1 )
End Function

Function Distance#(x1#, y1#, x2#, y2#)
	Return(Sqr(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1))))
End Function


Function PutINIValue%(INI_sAppName$, INI_sSection$, INI_sKey$, INI_sValue$)
	
; Returns: True (Success) or False (Failed)
	
	INI_sSection = "[" + Trim$(INI_sSection) + "]"
	INI_sUpperSection$ = Upper$(INI_sSection)
	INI_sKey = Trim$(INI_sKey)
	INI_sValue = Trim$(INI_sValue)
	INI_sFilename$ = CurrentDir$() + "\"  + INI_sAppName
	
; Retrieve the INI data (if it exists)
	
	INI_sContents$= INI_FileToString(INI_sFilename)
	
; (Re)Create the INI file updating/adding the SECTION, KEY and VALUE
	
	INI_bWrittenKey% = False
	INI_bSectionFound% = False
	INI_sCurrentSection$ = ""
	
	INI_lFileHandle = WriteFile(INI_sFilename)
	If INI_lFileHandle = 0 Then Return False ; Create file failed!
	
	INI_lOldPos% = 1
	INI_lPos% = Instr(INI_sContents, Chr$(0))
	
	While (INI_lPos <> 0)
		
		INI_sTemp$ =Trim$(Mid$(INI_sContents, INI_lOldPos, (INI_lPos - INI_lOldPos)))
		
		If (INI_sTemp <> "") Then
			
			If Left$(INI_sTemp, 1) = "[" And Right$(INI_sTemp, 1) = "]" Then
				
				; Process SECTION
				
				If (INI_sCurrentSection = INI_sUpperSection) And (INI_bWrittenKey = False) Then
					INI_bWrittenKey = INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue)
				End If
				INI_sCurrentSection = Upper$(INI_CreateSection(INI_lFileHandle, INI_sTemp))
				If (INI_sCurrentSection = INI_sUpperSection) Then INI_bSectionFound = True
				
			Else
				
				; KEY=VALUE
				
				lEqualsPos% = Instr(INI_sTemp, "=")
				If (lEqualsPos <> 0) Then
					If (INI_sCurrentSection = INI_sUpperSection) And (Upper$(Trim$(Left$(INI_sTemp, (lEqualsPos - 1)))) = Upper$(INI_sKey)) Then
						If (INI_sValue <> "") Then INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
						INI_bWrittenKey = True
					Else
						WriteLine INI_lFileHandle, INI_sTemp
					End If
				End If
				
			End If
			
		End If
		
		; Move through the INI file...
		
		INI_lOldPos = INI_lPos + 1
		INI_lPos% = Instr(INI_sContents, Chr$(0), INI_lOldPos)
		
	Wend
	
	; KEY wasn't found in the INI file - Append a new SECTION if required and create our KEY=VALUE line
	
	If (INI_bWrittenKey = False) Then
		If (INI_bSectionFound = False) Then INI_CreateSection INI_lFileHandle, INI_sSection
		INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
	End If
	
	CloseFile INI_lFileHandle
	
	Return True ; Success
	
End Function

Function INI_FileToString$(INI_sFilename$)
	
	INI_sString$ = ""
	INI_lFileHandle% = ReadFile(INI_sFilename)
	If INI_lFileHandle <> 0 Then
		While Not(Eof(INI_lFileHandle))
			INI_sString = INI_sString + ReadLine$(INI_lFileHandle) + Chr$(0)
		Wend
		CloseFile INI_lFileHandle
	End If
	Return INI_sString
	
End Function

Function INI_CreateSection$(INI_lFileHandle%, INI_sNewSection$)
	
	If FilePos(INI_lFileHandle) <> 0 Then WriteLine INI_lFileHandle, "" ; Blank line between sections
	WriteLine INI_lFileHandle, INI_sNewSection
	Return INI_sNewSection
	
End Function

Function INI_CreateKey%(INI_lFileHandle%, INI_sKey$, INI_sValue$)
	
	WriteLine INI_lFileHandle, INI_sKey + "=" + INI_sValue
	Return True
	
End Function






;~IDEal Editor Parameters:
;~F#C#15#1E#49#22E#25C#26D#284#297#2A3#2B4#2DD#2F2#30F#327#337#34C#35A#360#364
;~F#368#372#376#37B#3CB#3D9#3E1
;~C#Blitz3D