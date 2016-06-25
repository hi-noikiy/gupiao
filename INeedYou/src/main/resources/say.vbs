Set a = WScript.Arguments
if a.length=1 Then
CreateObject("SAPI.SpVoice").Speak a(0)
End if