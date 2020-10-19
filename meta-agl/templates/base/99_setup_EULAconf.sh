case "$EULA_ACCEPT" in
	1)
		echo "" >> $BUILDDIR/conf/local.conf
		echo "# EULA" >> $BUILDDIR/conf/local.conf
		if test x"" = x"$EULA_FLAG_NAME"; then
			echo "ACCEPT_EULA_$MACHINE = \"1\"" >> $BUILDDIR/conf/local.conf
		else
			echo "$EULA_FLAG_NAME = \"1\"" >> $BUILDDIR/conf/local.conf
		fi
		;;
	*)
		;;
esac

