if $BOOTMODE; then
    ui_print "- Installing from Magisk / KernelSU app"
else
    ui_print "*********************************************************"
    ui_print "! Install from recovery is NOT supported"
    ui_print "! Recovery sucks"
    ui_print "! Please install from Magisk / KernelSU app"
    abort    "*********************************************************"
fi

# Check if safetynet-fix is installed
if [ -d "/data/adb/modules/safetynet-fix" ]; then
	ui_print "! safetynet-fix module will be disabled"
    touch "/data/adb/modules/safetynet-fix/disable"
fi

# Android < 8.0
if [ "$API" -lt 26 ]; then
    abort "!!! You can't use this module on Android < 8.0"
fi