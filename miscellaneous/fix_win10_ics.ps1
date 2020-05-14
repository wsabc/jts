Set-ExecutionPolicy RemoteSigned

$NetShare = New-Object -ComObject HNetCfg.HNetShare
$wlan = $null
$ethernet = $null

foreach ($int in $NetShare.EnumEveryConnection) {
  $props = $NetShare.NetConnectionProps.Invoke($int)
  if ($props.Name -eq "Wi-Fi 4") {
    $wlan = $int;
  }
  if ($props.Name -eq "vEthernet (ICS)") {
    $ethernet = $int;
  }
}

$wlanConfig = $NetShare.INetSharingConfigurationForINetConnection.Invoke($wlan);
$ethernetConfig = $NetShare.INetSharingConfigurationForINetConnection.Invoke($ethernet);

$wlanConfig.DisableSharing();
$ethernetConfig.DisableSharing();

$wlanConfig.EnableSharing(0);
$ethernetConfig.EnableSharing(1);