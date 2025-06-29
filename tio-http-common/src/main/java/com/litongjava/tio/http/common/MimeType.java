package com.litongjava.tio.http.common;

/**
 * 本类大部分摘自github的一个项目（当时找过后，忘记记录地址，后面工到后补充进来），部分为tio作者补充，感谢作者的贡献
 * @author tanyaowu
 * 2017年8月5日 上午10:36:37
 */
public enum MimeType {

  /* Preferred Common Types */
  APPLICATION_PDF_PDF("application/pdf", "pdf"),

  APPLICATION_ZIP_ZIP("application/zip", "zip"),

  AUDIO_MPEG3_MP3("audio/mpeg3", "mp3"),

  IMAGE_GIF_GIF("image/gif", "gif"),

  TEXT_HTML_HTML("text/html", "html"),

  IMAGE_JPEG_JPG("image/jpeg", "jpg"),

  IMAGE_PNG_PNG("image/png", "png"),

  TEXT_CSS_CSS("text/css", "css"),

  TEXT_CSV_CSV("text/csv", "csv"),

  TEXT_JAVASCRIPT_JS("text/javascript", "js"),

  TEXT_PLAIN_TXT("text/plain", "txt"),

  APPLICATION_JSON("application/json", "json"),

  VIDEO_AVI_AVI("video/avi", "avi"),

  VIDEO_QUICKTIME_MOV("video/quicktime", "mov"),

  VIDEO_MPEG_MP4("video/mp4", "mp4"),

  VIDEO_MATROSKA_MKV("video/x-matroska", "mkv"),

  /* Other */
  APPLICATION_XBYTECODEPYTHON_PYC("application/x-bytecode.python", "pyc"),

  APPLICATION_ACAD_DWG("application/acad", "dwg"),

  APPLICATION_ARJ_ARJ("application/arj", "arj"),

  APPLICATION_BASE64_MM("application/base64", "mm"),

  APPLICATION_BASE64_MME("application/base64", "mme"),

  APPLICATION_BINHEX4_HQX("application/binhex4", "hqx"),

  APPLICATION_BINHEX_HQX("application/binhex", "hqx"),

  APPLICATION_BOOK_BOO("application/book", "boo"),

  APPLICATION_BOOK_BOOK("application/book", "book"),

  APPLICATION_CDF_CDF("application/cdf", "cdf"),

  APPLICATION_CLARISCAD_CCAD("application/clariscad", "ccad"),

  APPLICATION_COMMONGROUND_DP("application/commonground", "dp"),

  APPLICATION_DRAFTING_DRW("application/drafting", "drw"),

  APPLICATION_DSPTYPE_TSP("application/dsptype", "tsp"),

  APPLICATION_DXF_DXF("application/dxf", "dxf"),

  APPLICATION_ECMASCRIPT_JS("application/ecmascript", "js"),

  APPLICATION_ENVOY_EVY("application/envoy", "evy"),

  APPLICATION_EXCEL_XL("application/excel", "xl"),

  APPLICATION_EXCEL_XLA("application/excel", "xla"),

  APPLICATION_EXCEL_XLB("application/excel", "xlb"),

  APPLICATION_EXCEL_XLC("application/excel", "xlc"),

  APPLICATION_EXCEL_XLD("application/excel", "xld"),

  APPLICATION_EXCEL_XLK("application/excel", "xlk"),

  APPLICATION_EXCEL_XLL("application/excel", "xll"),

  APPLICATION_EXCEL_XLM("application/excel", "xlm"),

  APPLICATION_EXCEL_XLS("application/excel", "xls"),

  APPLICATION_EXCEL_XLT("application/excel", "xlt"),

  APPLICATION_EXCEL_XLV("application/excel", "xlv"),

  APPLICATION_EXCEL_XLW("application/excel", "xlw"),

  APPLICATION_EXCEL_XLX("application/excel", "xlx"),

  APPLICATION_FRACTALS_FIF("application/fractals", "fif"),

  APPLICATION_FREELOADER_FRL("application/freeloader", "frl"),

  APPLICATION_FUTURESPLASH_SPL("application/futuresplash", "spl"),

  APPLICATION_GNUTAR_TGZ("application/gnutar", "tgz"),

  APPLICATION_GROUPWISE_VEW("application/groupwise", "vew"),

  APPLICATION_HLP_HLP("application/hlp", "hlp"),

  APPLICATION_HTA_HTA("application/hta", "hta"),

  APPLICATION_IDEAS_UNV("application/i-deas", "unv"),

  APPLICATION_IGES_IGES("application/iges", "iges"),

  APPLICATION_IGES_IGS("application/iges", "igs"),

  APPLICATION_INF_INF("application/inf", "inf"),

  APPLICATION_JAVABYTECODE_CLASS("application/java-byte-code", "class"),

  APPLICATION_JAVA_CLASS("application/java", "class"),

  APPLICATION_JAVASCRIPT_JS("application/javascript", "js"),

  APPLICATION_LHA_LHA("application/lha", "lha"),

  APPLICATION_LZX_LZX("application/lzx", "lzx"),

  APPLICATION_MACBINARY_BIN("application/mac-binary", "bin"),

  APPLICATION_MACBINARY_BINARY("application/macbinary", "bin"),

  APPLICATION_MACBINHEX40_HQX("application/mac-binhex40", "hqx"),

  APPLICATION_MACBINHEX_HQX("application/mac-binhex", "hqx"),

  APPLICATION_MACCOMPACTPRO_CPT("application/mac-compactpro", "cpt"),

  APPLICATION_MARC_MRC("application/marc", "mrc"),

  APPLICATION_MBEDLET_MBD("application/mbedlet", "mbd"),

  APPLICATION_MCAD_MCD("application/mcad", "mcd"),

  APPLICATION_MIME_APS("application/mime", "aps"),

  APPLICATION_MSPOWERPOINT_POT("application/mspowerpoint", "pot"),

  APPLICATION_MSPOWERPOINT_PPS("application/mspowerpoint", "pps"),

  APPLICATION_MSPOWERPOINT_PPT("application/mspowerpoint", "ppt"),

  APPLICATION_MSPOWERPOINT_PPTX("application/mspowerpoint", "pptx"),

  APPLICATION_MSPOWERPOINT_PPZ("application/mspowerpoint", "ppz"),

  APPLICATION_MSWORD_DOC("application/msword", "doc"),

  APPLICATION_MSWORD_DOCX("application/msword", "docx"),

  APPLICATION_MSWORD_DOT("application/msword", "dot"),

  APPLICATION_MSWORD_W6W("application/msword", "w6w"),

  APPLICATION_MSWORD_WIZ("application/msword", "wiz"),

  APPLICATION_MSWORD_WORD("application/msword", "word"),

  APPLICATION_MSWRITE_WRI("application/mswrite", "wri"),

  APPLICATION_NETMC_MCP("application/netmc", "mcp"),

  APPLICATION_OCTETSTREAM_A("application/octet-stream", "a"),

  APPLICATION_OCTETSTREAM_ARC("application/octet-stream", "arc"),

  APPLICATION_OCTETSTREAM_ARJ("application/octet-stream", "arj"),

  APPLICATION_OCTETSTREAM_BIN("application/octet-stream", "bin"),

  APPLICATION_OCTETSTREAM_COM("application/octet-stream", "com"),

  APPLICATION_OCTETSTREAM_DUMP("application/octet-stream", "dump"),

  APPLICATION_OCTETSTREAM_EXE("application/octet-stream", "exe"),

  APPLICATION_OCTETSTREAM_LHA("application/octet-stream", "lha"),

  APPLICATION_OCTETSTREAM_LHX("application/octet-stream", "lhx"),

  APPLICATION_OCTETSTREAM_LZH("application/octet-stream", "lzh"),

  APPLICATION_OCTETSTREAM_LZX("application/octet-stream", "lzx"),

  APPLICATION_OCTETSTREAM_O("application/octet-stream", "o"),

  APPLICATION_OCTETSTREAM_PSD("application/octet-stream", "psd"),

  APPLICATION_OCTETSTREAM_SAVEME("application/octet-stream", "saveme"),

  APPLICATION_OCTETSTREAM_UU("application/octet-stream", "uu"),

  APPLICATION_OCTETSTREAM_ZOO("application/octet-stream", "zoo"),

  APPLICATION_ODA_ODA("application/oda", "oda"),

  APPLICATION_PKCS10_P10("application/pkcs10", "p10"),

  APPLICATION_PKCS12_P12("application/pkcs-12", "p12"),

  APPLICATION_PKCS7MIME_P7C("application/pkcs7-mime", "p7c"),

  APPLICATION_PKCS7MIME_P7M("application/pkcs7-mime", "p7m"),

  APPLICATION_PKCS7SIGNATURE_P7S("application/pkcs7-signature", "p7s"),

  APPLICATION_PKCSCRL_CRL("application/pkcs-crl", "crl"),

  APPLICATION_PKIXCERT_CER("application/pkix-cert", "cer"),

  APPLICATION_PKIXCERT_CRT("application/pkix-cert", "crt"),

  APPLICATION_PKIXCRL_CRL("application/pkix-crl", "crl"),

  APPLICATION_PLAIN_TEXT("application/plain", "text"),

  APPLICATION_POSTSCRIPT_AI("application/postscript", "ai"),

  APPLICATION_POSTSCRIPT_EPS("application/postscript", "eps"),

  APPLICATION_POSTSCRIPT_PS("application/postscript", "ps"),

  APPLICATION_POWERPOINT_PPT("application/powerpoint", "ppt"),

  APPLICATION_PRO_ENG_PART("application/pro_eng", "part"),

  APPLICATION_PRO_ENG_PRT("application/pro_eng", "prt"),

  APPLICATION_RINGINGTONES_RNG("application/ringing-tones", "rng"),

  APPLICATION_RTF_RTF("application/rtf", "rtf"),

  APPLICATION_RTF_RTX("application/rtf", "rtx"),

  APPLICATION_SDP_SDP("application/sdp", "sdp"),

  APPLICATION_SEA_SEA("application/sea", "sea"),

  APPLICATION_SET_SET("application/set", "set"),

  APPLICATION_SLA_STL("application/sla", "stl"),

  APPLICATION_SMIL_SMI("application/smil", "smi"),

  APPLICATION_SMIL_SMIL("application/smil", "smil"),

  APPLICATION_SOLIDS_SOL("application/solids", "sol"),

  APPLICATION_SOUNDER_SDR("application/sounder", "sdr"),

  APPLICATION_STEP_STEP("application/step", "step"),

  APPLICATION_STEP_STP("application/step", "stp"),

  APPLICATION_STREAMINGMEDIA_SSM("application/streamingmedia", "ssm"),

  APPLICATION_TOOLBOOK_TBK("application/toolbook", "tbk"),

  APPLICATION_VDA_VDA("application/vda", "vda"),

  APPLICATION_VNDFDF_FDF("application/vnd.fdf", "fdf"),

  APPLICATION_VNDHPHPGL_HGL("application/vnd.hp-hpgl", "hgl"),

  APPLICATION_VNDHPHPGL_HPG("application/vnd.hp-hpgl", "hpg"),

  APPLICATION_VNDHPHPGL_HPGL("application/vnd.hp-hpgl", "hpgl"),

  APPLICATION_VNDHPPCL_PCL("application/vnd.hp-pcl", "pcl"),

  APPLICATION_VNDMSEXCEL_XLB("application/vnd.ms-excel", "xlb"),

  APPLICATION_VNDMSEXCEL_XLC("application/vnd.ms-excel", "xlc"),

  APPLICATION_VNDMSEXCEL_XLL("application/vnd.ms-excel", "xll"),

  APPLICATION_VNDMSEXCEL_XLM("application/vnd.ms-excel", "xlm"),

  APPLICATION_VNDMSEXCEL_XLS("application/vnd.ms-excel", "xls"),

  APPLICATION_VNDMSEXCEL_XLW("application/vnd.ms-excel", "xlw"),

  APPLICATION_VNDMSPKICERTSTORE_SST("application/vnd.ms-pki.certstore", "sst"),

  APPLICATION_VNDMSPKIPKO_PKO("application/vnd.ms-pki.pko", "pko"),

  APPLICATION_VNDMSPKISECCAT_CAT("application/vnd.ms-pki.seccat", "cat"),

  APPLICATION_VNDMSPKISTL_STL("application/vnd.ms-pki.stl", "stl"),

  APPLICATION_VNDMSPOWERPOINT_POT("application/vnd.ms-powerpoint", "pot"),

  APPLICATION_VNDMSPOWERPOINT_PPA("application/vnd.ms-powerpoint", "ppa"),

  APPLICATION_VNDMSPOWERPOINT_PPS("application/vnd.ms-powerpoint", "pps"),

  APPLICATION_VNDMSPOWERPOINT_PPT("application/vnd.ms-powerpoint", "ppt"),

  APPLICATION_VNDMSPOWERPOINT_PWZ("application/vnd.ms-powerpoint", "pwz"),

  APPLICATION_VNDMSPROJECT_MPP("application/vnd.ms-project", "mpp"),

  APPLICATION_VNDNOKIACONFIGURATIONMESSAGE_NCM("application/vnd.nokia.configuration-message", "ncm"),

  APPLICATION_VNDNOKIARINGINGTONE_RNG("application/vnd.nokia.ringing-tone", "rng"),

  APPLICATION_VNDRNREALMEDIA_RM("application/vnd.rn-realmedia", "rm"),

  APPLICATION_VNDRNREALPLAYER_RNX("application/vnd.rn-realplayer", "rnx"),

  APPLICATION_VNDWAPWMLC_WMLC("application/vnd.wap.wmlc", "wmlc"),

  APPLICATION_VNDWAPWMLSCRIPTC_WMLSC("application/vnd.wap.wmlscriptc", "wmlsc"),

  APPLICATION_VNDXARA_WEB("application/vnd.xara", "web"),

  APPLICATION_VOCALTECMEDIADESC_VMD("application/vocaltec-media-desc", "vmd"),

  APPLICATION_VOCALTECMEDIAFILE_VMF("application/vocaltec-media-file", "vmf"),

  APPLICATION_WORDPERFECT60WP5("application/wordperfect6.0", "wp5"),

  APPLICATION_WORDPERFECT60_W60("application/wordperfect6.0", "w60"),

  APPLICATION_WORDPERFECT61_W61("application/wordperfect6.1", "w61"),

  APPLICATION_WORDPERFECT_WP("application/wordperfect", "wp"),

  APPLICATION_WORDPERFECT_WP5("application/wordperfect", "wp5"),

  APPLICATION_WORDPERFECT_WP6("application/wordperfect", "wp6"),

  APPLICATION_WORDPERFECT_WPD("application/wordperfect", "wpd"),

  APPLICATION_X123_WK1("application/x-123", "wk1"),

  APPLICATION_XAIM_AIM("application/x-aim", "aim"),

  APPLICATION_XAUTHORWAREBIN_AAB("application/x-authorware-bin", "aab"),

  APPLICATION_XAUTHORWAREMAP_AAM("application/x-authorware-map", "aam"),

  APPLICATION_XAUTHORWARESEG_AAS("application/x-authorware-seg", "aas"),

  APPLICATION_XBCPIO_BCPIO("application/x-bcpio", "bcpio"),

  APPLICATION_XBINARY_BIN("application/x-binary", "bin"),

  APPLICATION_XBINHEX40_HQX("application/x-binhex40", "hqx"),

  APPLICATION_XBSH_BSH("application/x-bsh", "bsh"),

  APPLICATION_XBSH_SH("application/x-bsh", "sh"),

  APPLICATION_XBSH_SHAR("application/x-bsh", "shar"),

  APPLICATION_XBYTECODEELISPCOMPILED_ELC("application/x-bytecode.elisp (compiled elisp)", "elc"),

  APPLICATION_XBZIP2_BOZ("application/x-bzip2", "boz"),

  APPLICATION_XBZIP2_BZ2("application/x-bzip2", "bz2"),

  APPLICATION_XBZIP_BZ("application/x-bzip", "bz"),

  APPLICATION_XCDF_CDF("application/x-cdf", "cdf"),

  APPLICATION_XCDLINK_VCD("application/x-cdlink", "vcd"),

  APPLICATION_XCHAT_CHA("application/x-chat", "cha"),

  APPLICATION_XCHAT_CHAT("application/x-chat", "chat"),

  APPLICATION_XCMURASTER_RAS("application/x-cmu-raster", "ras"),

  APPLICATION_XCOCOA_CCO("application/x-cocoa", "cco"),

  APPLICATION_XCOMPACTPRO_CPT("application/x-compactpro", "cpt"),

  APPLICATION_XCOMPRESSED_GZ("application/x-compressed", "gz"),

  APPLICATION_XCOMPRESSED_TGZ("application/x-compressed", "tgz"),

  APPLICATION_XCOMPRESSED_Z("application/x-compressed", "z"),

  APPLICATION_XCOMPRESSED_ZIP("application/x-compressed", "zip"),

  APPLICATION_XCOMPRESS_Z("application/x-compress", "z"),

  APPLICATION_XCONFERENCE_NSC("application/x-conference", "nsc"),

  APPLICATION_XCPIO_CPIO("application/x-cpio", "cpio"),

  APPLICATION_XCPT_CPT("application/x-cpt", "cpt"),

  APPLICATION_XCSH_CSH("application/x-csh", "csh"),

  APPLICATION_XDEEPV_DEEPV("application/x-deepv", "deepv"),

  APPLICATION_XDIRECTOR_DCR("application/x-director", "dcr"),

  APPLICATION_XDIRECTOR_DIR("application/x-director", "dir"),

  APPLICATION_XDIRECTOR_DXR("application/x-director", "dxr"),

  APPLICATION_XDVI_DVI("application/x-dvi", "dvi"),

  APPLICATION_XELC_ELC("application/x-elc", "elc"),

  APPLICATION_XENVOY_ENV("application/x-envoy", "env"),

  APPLICATION_XENVOY_EVY("application/x-envoy", "evy"),

  APPLICATION_XESREHBER_ES("application/x-esrehber", "es"),

  APPLICATION_XEXCEL_XLA("application/x-excel", "xla"),

  APPLICATION_XEXCEL_XLB("application/x-excel", "xlb"),

  APPLICATION_XEXCEL_XLC("application/x-excel", "xlc"),

  APPLICATION_XEXCEL_XLD("application/x-excel", "xld"),

  APPLICATION_XEXCEL_XLK("application/x-excel", "xlk"),

  APPLICATION_XEXCEL_XLL("application/x-excel", "xll"),

  APPLICATION_XEXCEL_XLM("application/x-excel", "xlm"),

  APPLICATION_XEXCEL_XLS("application/x-excel", "xls"),

  APPLICATION_XEXCEL_XLT("application/x-excel", "xlt"),

  APPLICATION_XEXCEL_XLV("application/x-excel", "xlv"),

  APPLICATION_XEXCEL_XLW("application/x-excel", "xlw"),

  APPLICATION_XFRAME_MIF("application/x-frame", "mif"),
  //
  APPLICATION_XFREELANCE_PRE("application/x-freelance", "pre"), APPLICATION_XGSP_GSP("application/x-gsp", "gsp"), APPLICATION_XGSS_GSS("application/x-gss", "gss"),
  APPLICATION_XGTAR_GTAR("application/x-gtar", "gtar"), APPLICATION_XGZIP_GZ("application/x-gzip", "gz"), APPLICATION_XGZIP_GZIP("application/x-gzip", "gzip"),
  APPLICATION_XHDF_HDF("application/x-hdf", "hdf"), APPLICATION_XHELPFILE_HELP("application/x-helpfile", "help"), APPLICATION_XHELPFILE_HLP("application/x-helpfile", "hlp"),
  APPLICATION_XHTTPDIMAP_IMAP("application/x-httpd-imap", "imap"), APPLICATION_XIMA_IMA("application/x-ima", "ima"), APPLICATION_XINTERNETTSIGNUP_INS("application/x-internett-signup", "ins"),
  APPLICATION_XINVENTOR_IV("application/x-inventor", "iv"), APPLICATION_XIP2_IP("application/x-ip2", "ip"), APPLICATION_XJAVACLASS_CLASS("application/x-java-class", "class"),
  APPLICATION_XJAVACOMMERCE_JCM("application/x-java-commerce", "jcm"), APPLICATION_XJAVASCRIPT_JS("application/x-javascript", "js"), APPLICATION_XKOAN_SKD("application/x-koan", "skd"),
  APPLICATION_XKOAN_SKM("application/x-koan", "skm"), APPLICATION_XKOAN_SKP("application/x-koan", "skp"), APPLICATION_XKOAN_SKT("application/x-koan", "skt"),
  APPLICATION_XKSH_KSH("application/x-ksh", "ksh"), APPLICATION_XLATEX_LATEX("application/x-latex", "latex"), APPLICATION_XLATEX_LTX("application/x-latex", "ltx"),
  APPLICATION_XLHA_LHA("application/x-lha", "lha"), APPLICATION_XLISP_LSP("application/x-lisp", "lsp"), APPLICATION_XLIVESCREEN_IVY("application/x-livescreen", "ivy"),
  APPLICATION_XLOTUSSCREENCAM_SCM("application/x-lotusscreencam", "scm"), APPLICATION_XLOTUS_WQ1("application/x-lotus", "wq1"), APPLICATION_XLZH_LZH("application/x-lzh", "lzh"),
  APPLICATION_XLZX_LZX("application/x-lzx", "lzx"), APPLICATION_XMACBINARY_BIN("application/x-macbinary", "bin"), APPLICATION_XMACBINHEX40_HQX("application/x-mac-binhex40", "hqx"),
  APPLICATION_XMAGICCAPPACKAGE10_MC$("application/x-magic-cap-package-1.0", "mc$"), APPLICATION_XMATHCAD_MCD("application/x-mathcad", "mcd"), APPLICATION_XMEME_MM("application/x-meme", "mm"),
  APPLICATION_XMIDI_MID("application/x-midi", "mid"), APPLICATION_XMIDI_MIDI("application/x-midi", "midi"),
  //
  APPLICATION_XMIF_MIF("application/x-mif", "mif"),
  //
  APPLICATION_XMIXTRANSFER_NIX("application/x-mix-transfer", "nix"),
  //
  APPLICATION_XML_XML("application/xml", "xml"),
  //
  APPLICATION_XMPLAYER2_ASX("application/x-mplayer2", "asx"),
  //
  APPLICATION_XMSEXCEL_XLA("application/x-msexcel", "xla"), APPLICATION_XMSEXCEL_XLS("application/x-msexcel", "xls"), APPLICATION_XMSEXCEL_XLW("application/x-msexcel", "xlw"),
  APPLICATION_XMSPOWERPOINT_PPT("application/x-mspowerpoint", "ppt"), APPLICATION_XNAVIANIMATION_ANI("application/x-navi-animation", "ani"), APPLICATION_XNAVIDOC_NVD("application/x-navidoc", "nvd"),
  APPLICATION_XNAVIMAP_MAP("application/x-navimap", "map"), APPLICATION_XNAVISTYLE_STL("application/x-navistyle", "stl"), APPLICATION_XNETCDF_CDF("application/x-netcdf", "cdf"),
  APPLICATION_XNETCDF_NC("application/x-netcdf", "nc"), APPLICATION_XNEWTONCOMPATIBLEPKG_PKG("application/x-newton-compatible-pkg", "pkg"),
  APPLICATION_XNOKIA9000COMMUNICATORADDONSOFTWARE_AOS("application/x-nokia-9000-communicator-add-on-software", "aos"), APPLICATION_XOMCDATAMAKER_OMCD("application/x-omcdatamaker", "omcd"),
  APPLICATION_XOMCREGERATOR_OMCR("application/x-omcregerator", "omcr"), APPLICATION_XOMC_OMC("application/x-omc", "omc"), APPLICATION_XPAGEMAKER_PM4("application/x-pagemaker", "pm4"),
  APPLICATION_XPAGEMAKER_PM5("application/x-pagemaker", "pm5"), APPLICATION_XPCL_PCL("application/x-pcl", "pcl"), APPLICATION_XPIXCLSCRIPT_PLX("application/x-pixclscript", "plx"),
  APPLICATION_XPKCS10_P10("application/x-pkcs10", "p10"), APPLICATION_XPKCS12_P12("application/x-pkcs12", "p12"), APPLICATION_XPKCS7CERTIFICATES_SPC("application/x-pkcs7-certificates", "spc"),
  APPLICATION_XPKCS7CERTREQRESP_P7R("application/x-pkcs7-certreqresp", "p7r"), APPLICATION_XPKCS7MIME_P7C("application/x-pkcs7-mime", "p7c"),
  APPLICATION_XPKCS7MIME_P7M("application/x-pkcs7-mime", "p7m"), APPLICATION_XPKCS7SIGNATURE_P7A("application/x-pkcs7-signature", "p7a"), APPLICATION_XPOINTPLUS_CSS("application/x-pointplus", "css"),
  APPLICATION_XPORTABLEANYMAP_PNM("application/x-portable-anymap", "pnm"), APPLICATION_XPROJECT_MPC("application/x-project", "mpc"), APPLICATION_XPROJECT_MPT("application/x-project", "mpt"),
  APPLICATION_XPROJECT_MPV("application/x-project", "mpv"), APPLICATION_XPROJECT_MPX("application/x-project", "mpx"), APPLICATION_XQPRO_WB1("application/x-qpro", "wb1"),
  APPLICATION_XRTF_RTF("application/x-rtf", "rtf"), APPLICATION_XSDP_SDP("application/x-sdp", "sdp"), APPLICATION_XSEA_SEA("application/x-sea", "sea"),
  APPLICATION_XSEELOGO_SL("application/x-seelogo", "sl"), APPLICATION_XSHAR_SH("application/x-shar", "sh"), APPLICATION_XSHAR_SHAR("application/x-shar", "shar"),
  APPLICATION_XSHOCKWAVEFLASH_SWF("application/x-shockwave-flash", "swf"), APPLICATION_XSH_SH("application/x-sh", "sh"), APPLICATION_XSIT_SIT("application/x-sit", "sit"),
  APPLICATION_XSPRITE_SPR("application/x-sprite", "spr"), APPLICATION_XSPRITE_SPRITE("application/x-sprite", "sprite"), APPLICATION_XSTUFFIT_SIT("application/x-stuffit", "sit"),
  APPLICATION_XSV4CPIO_SV4CPIO("application/x-sv4cpio", "sv4cpio"), APPLICATION_XSV4CRC_SV4CRC("application/x-sv4crc", "sv4crc"), APPLICATION_XTAR_TAR("application/x-tar", "tar"),
  APPLICATION_XTBOOK_SBK("application/x-tbook", "sbk"), APPLICATION_XTBOOK_TBK("application/x-tbook", "tbk"), APPLICATION_XTCL_TCL("application/x-tcl", "tcl"),
  APPLICATION_XTEXINFO_TEXI("application/x-texinfo", "texi"), APPLICATION_XTEXINFO_TEXINFO("application/x-texinfo", "texinfo"), APPLICATION_XTEX_TEX("application/x-tex", "tex"),
  APPLICATION_XTROFFMAN_MAN("application/x-troff-man", "man"), APPLICATION_XTROFFME_ME("application/x-troff-me", "me"), APPLICATION_XTROFFMSVIDEO_AVI("application/x-troff-msvideo", "avi"),
  APPLICATION_XTROFFMS_MS("application/x-troff-ms", "ms"), APPLICATION_XTROFF_ROFF("application/x-troff", "roff"), APPLICATION_XTROFF_T("application/x-troff", "t"),
  APPLICATION_XTROFF_TR("application/x-troff", "tr"), APPLICATION_XUSTAR_USTAR("application/x-ustar", "ustar"), APPLICATION_XVISIO_VSD("application/x-visio", "vsd"),
  APPLICATION_XVISIO_VST("application/x-visio", "vst"), APPLICATION_XVISIO_VSW("application/x-visio", "vsw"), APPLICATION_XVNDAUDIOEXPLOSIONMZZ_MZZ("application/x-vnd.audioexplosion.mzz", "mzz"),
  APPLICATION_XVNDLSXPIX_XPIX("application/x-vnd.ls-xpix", "xpix"), APPLICATION_XVRML_VRML("application/x-vrml", "vrml"), APPLICATION_XWAISSOURCE_SRC("application/x-wais-source", "src"),
  APPLICATION_XWAISSOURCE_WSRC("application/x-wais-source", "wsrc"), APPLICATION_XWINHELP_HLP("application/x-winhelp", "hlp"), APPLICATION_XWINTALK_WTK("application/x-wintalk", "wtk"),
  APPLICATION_XWORLD_SVR("application/x-world", "svr"), APPLICATION_XWORLD_WRL("application/x-world", "wrl"), APPLICATION_XWPWIN_WPD("application/x-wpwin", "wpd"),
  APPLICATION_XWRI_WRI("application/x-wri", "wri"), APPLICATION_XX509CACERT_CER("application/x-x509-ca-cert", "cer"), APPLICATION_XX509CACERT_CRT("application/x-x509-ca-cert", "crt"),
  APPLICATION_XX509CACERT_DER("application/x-x509-ca-cert", "der"), APPLICATION_XX509USERCERT_CRT("application/x-x509-user-cert", "crt"),
  APPLICATION_XZIPCOMPRESSED_ZIP("application/x-zip-compressed", "zip"), AUDIO_AIFF_AIF("audio/aiff", "aif"), AUDIO_AIFF_AIFC("audio/aiff", "aifc"), AUDIO_AIFF_AIFF("audio/aiff", "aiff"),
  AUDIO_BASIC_AU("audio/basic", "au"), AUDIO_BASIC_SND("audio/basic", "snd"), AUDIO_IT_IT("audio/it", "it"), AUDIO_MAKEMYFUNK_PFUNK("audio/make.my.funk", "pfunk"),
  AUDIO_MAKE_FUNK("audio/make", "funk"), AUDIO_MAKE_MY("audio/make", "my"), AUDIO_MAKE_PFUNK("audio/make", "pfunk"), AUDIO_MIDI_KAR("audio/midi", "kar"), AUDIO_MIDI_MID("audio/midi", "mid"),
  AUDIO_MIDI_MIDI("audio/midi", "midi"), AUDIO_MID_RMI("audio/mid", "rmi"), AUDIO_MOD_MOD("audio/mod", "mod"), AUDIO_MPEG_M2A("audio/mpeg", "m2a"), AUDIO_MPEG_MP2("audio/mpeg", "mp2"),
  AUDIO_MPEG_MPA("audio/mpeg", "mpa"), AUDIO_MPEG_MPG("audio/mpeg", "mpg"), AUDIO_MPEG_MPGA("audio/mpeg", "mpga"), AUDIO_NSPAUDIO_LA("audio/nspaudio", "la"),
  AUDIO_NSPAUDIO_LMA("audio/nspaudio", "lma"), AUDIO_S3M_S3M("audio/s3m", "s3m"), AUDIO_TSPAUDIO_TSI("audio/tsp-audio", "tsi"), AUDIO_TSPLAYER_TSP("audio/tsplayer", "tsp"),
  AUDIO_VNDQCELP_QCP("audio/vnd.qcelp", "qcp"), AUDIO_VOC_VOC("audio/voc", "voc"), AUDIO_VOXWARE_VOX("audio/voxware", "vox"), AUDIO_WAV_WAV("audio/wav", "wav"),
  AUDIO_XADPCM_SND("audio/x-adpcm", "snd"), AUDIO_XAIFF_AIF("audio/x-aiff", "aif"), AUDIO_XAIFF_AIFC("audio/x-aiff", "aifc"), AUDIO_XAIFF_AIFF("audio/x-aiff", "aiff"),
  AUDIO_XAU_AU("audio/x-au", "au"), AUDIO_XGSM_GSD("audio/x-gsm", "gsd"), AUDIO_XGSM_GSM("audio/x-gsm", "gsm"), AUDIO_XJAM_JAM("audio/x-jam", "jam"), AUDIO_XLIVEAUDIO_LAM("audio/x-liveaudio", "lam"),
  AUDIO_XMIDI_MID("audio/x-midi", "mid"), AUDIO_XMIDI_MIDI("audio/x-midi", "midi"), AUDIO_XMID_MID("audio/x-mid", "mid"), AUDIO_XMID_MIDI("audio/x-mid", "midi"), AUDIO_XMOD_MOD("audio/x-mod", "mod"),
  AUDIO_XMPEG3_MP3("audio/x-mpeg-3", "mp3"), AUDIO_XMPEG_MP2("audio/x-mpeg", "mp2"), AUDIO_XMPEQURL_M3U("audio/x-mpequrl", "m3u"), AUDIO_XM_XM("audio/xm", "xm"),
  AUDIO_XNSPAUDIO_LA("audio/x-nspaudio", "la"), AUDIO_XNSPAUDIO_LMA("audio/x-nspaudio", "lma"), AUDIO_XPNREALAUDIOPLUGIN_RA("audio/x-pn-realaudio-plugin", "ra"),
  AUDIO_XPNREALAUDIOPLUGIN_RMP("audio/x-pn-realaudio-plugin", "rmp"), AUDIO_XPNREALAUDIOPLUGIN_RPM("audio/x-pn-realaudio-plugin", "rpm"), AUDIO_XPNREALAUDIO_RA("audio/x-pn-realaudio", "ra"),
  AUDIO_XPNREALAUDIO_RAM("audio/x-pn-realaudio", "ram"), AUDIO_XPNREALAUDIO_RM("audio/x-pn-realaudio", "rm"), AUDIO_XPNREALAUDIO_RMM("audio/x-pn-realaudio", "rmm"),
  AUDIO_XPNREALAUDIO_RMP("audio/x-pn-realaudio", "rmp"), AUDIO_XPSID_SID("audio/x-psid", "sid"), AUDIO_XREALAUDIO_RA("audio/x-realaudio", "ra"),
  AUDIO_XTWINVQPLUGIN_VQE("audio/x-twinvq-plugin", "vqe"), AUDIO_XTWINVQPLUGIN_VQL("audio/x-twinvq-plugin", "vql"), AUDIO_XTWINVQ_VQF("audio/x-twinvq", "vqf"),
  AUDIO_XVNDAUDIOEXPLOSIONMJUICEMEDIAFILE_MJF("audio/x-vnd.audioexplosion.mjuicemediafile", "mjf"), AUDIO_XVOC_VOC("audio/x-voc", "voc"), AUDIO_XWAV_WAV("audio/x-wav", "wav"),
  CHEMICAL_XPDB_PDB("chemical/x-pdb", "pdb"), CHEMICAL_XPDB_XYZ("chemical/x-pdb", "xyz"), DRAWING_XDWFOLD_dwf("drawing/x-dwf (old)", "dwf"), IMAGE_BMP_BM("image/bmp", "bm"),
  IMAGE_BMP_BMP("image/bmp", "bmp"), IMAGE_CMURASTER_RAS("image/cmu-raster", "ras"), IMAGE_CMURASTER_RAST("image/cmu-raster", "rast"), IMAGE_FIF_FIF("image/fif", "fif"),
  IMAGE_FLORIAN_FLO("image/florian", "flo"), IMAGE_FLORIAN_TURBOT("image/florian", "turbot"), IMAGE_G3FAX_G3("image/g3fax", "g3"), IMAGE_IEF_IEF("image/ief", "ief"),
  IMAGE_IEF_IEFS("image/ief", "iefs"), IMAGE_JPEG_JFIF("image/jpeg", "jfif"), IMAGE_JPEG_JFIFTBNL("image/jpeg", "jfif-tbnl"), IMAGE_JPEG_JPE("image/jpeg", "jpe"),
  IMAGE_JPEG_JPEG("image/jpeg", "jpeg"), IMAGE_JUTVISION_JUT("image/jutvision", "jut"), IMAGE_NAPLPS_NAP("image/naplps", "nap"), IMAGE_NAPLPS_NAPLPS("image/naplps", "naplps"),
  IMAGE_PICT_PIC("image/pict", "pic"), IMAGE_PICT_PICT("image/pict", "pict"), IMAGE_PJPEG_JFIF("image/pjpeg", "jfif"), IMAGE_PJPEG_JPE("image/pjpeg", "jpe"), IMAGE_PJPEG_JPEG("image/pjpeg", "jpeg"),
  IMAGE_PJPEG_JPG("image/pjpeg", "jpg"), IMAGE_PNG_XPNG("image/png", "x-png"), IMAGE_SVG_SVG("image/svg+xml", "svg"), IMAGE_SVG_SVGZ("image/svg+xml", "svgz"), IMAGE_TIFF_TIF("image/tiff", "tif"),
  IMAGE_TIFF_TIFF("image/tiff", "tiff"), IMAGE_VASA_MCF("image/vasa", "mcf"), IMAGE_VNDDWG_DWG("image/vnd.dwg", "dwg"), IMAGE_VNDDWG_DXF("image/vnd.dwg", "dxf"),
  IMAGE_VNDDWG_SVF("image/vnd.dwg", "svf"), IMAGE_VNDFPX_FPX("image/vnd.fpx", "fpx"), IMAGE_VNDNETFPX_FPX("image/vnd.net-fpx", "fpx"), IMAGE_VNDRNREALFLASH_RF("image/vnd.rn-realflash", "rf"),
  IMAGE_VNDRNREALPIX_RP("image/vnd.rn-realpix", "rp"), IMAGE_VNDWAPWBMP_WBMP("image/vnd.wap.wbmp", "wbmp"), IMAGE_VNDXIFF_XIF("image/vnd.xiff", "xif"), IMAGE_XBM_XBM("image/xbm", "xbm"),
  IMAGE_XCMURASTER_RAS("image/x-cmu-raster", "ras"), IMAGE_XDWG_DWG("image/x-dwg", "dwg"), IMAGE_XDWG_DXF("image/x-dwg", "dxf"), IMAGE_XDWG_SVF("image/x-dwg", "svf"),
  IMAGE_XICON_ICO("image/x-icon", "ico"), IMAGE_XJG_ART("image/x-jg", "art"), IMAGE_XJPS_JPS("image/x-jps", "jps"), IMAGE_XNIFF_NIF("image/x-niff", "nif"), IMAGE_XNIFF_NIFF("image/x-niff", "niff"),
  IMAGE_XPCX_PCX("image/x-pcx", "pcx"), IMAGE_XPICT_PCT("image/x-pict", "pct"), IMAGE_XPM_XPM("image/xpm", "xpm"), IMAGE_XPORTABLEANYMAP_PNM("image/x-portable-anymap", "pnm"),
  IMAGE_XPORTABLEBITMAP_PBM("image/x-portable-bitmap", "pbm"), IMAGE_XPORTABLEGRAYMAP_PGM("image/x-portable-graymap", "pgm"), IMAGE_XPORTABLEGREYMAP_PGM("image/x-portable-greymap", "pgm"),
  IMAGE_XPORTABLEPIXMAP_PPM("image/x-portable-pixmap", "ppm"), IMAGE_XQUICKTIME_QIF("image/x-quicktime", "qif"), IMAGE_XQUICKTIME_QTI("image/x-quicktime", "qti"),
  IMAGE_XQUICKTIME_QTIF("image/x-quicktime", "qtif"), IMAGE_XRGB_RGB("image/x-rgb", "rgb"), IMAGE_XTIFF_TIF("image/x-tiff", "tif"), IMAGE_XTIFF_TIFF("image/x-tiff", "tiff"),
  IMAGE_XWINDOWSBMP_BMP("image/x-windows-bmp", "bmp"), IMAGE_XXBITMAP_XBM("image/x-xbitmap", "xbm"), IMAGE_XXBM_XBM("image/x-xbm", "xbm"), IMAGE_XXPIXMAP_PM("image/x-xpixmap", "pm"),
  IMAGE_XXPIXMAP_XPM("image/x-xpixmap", "xpm"), IMAGE_XXWD_XWD("image/x-xwd", "xwd"), IMAGE_XXWINDOWDUMP_XWD("image/x-xwindowdump", "xwd"), IWORLD_IVRML_IVR("i-world/i-vrml", "ivr"),
  MESSAGE_RFC822_MHT("message/rfc822", "mht"), MESSAGE_RFC822_MHTML("message/rfc822", "mhtml"), MESSAGE_RFC822_MIME("message/rfc822", "mime"), MODEL_IGES_IGES("model/iges", "iges"),
  MODEL_IGES_IGS("model/iges", "igs"), MODEL_VNDDWF_DWF("model/vnd.dwf", "dwf"), MODEL_VRML_VRML("model/vrml", "vrml"), MODEL_VRML_WRL("model/vrml", "wrl"), MODEL_VRML_WRZ("model/vrml", "wrz"),
  MODEL_XPOV_POV("model/x-pov", "pov"), MULTIPART_XGZIP_GZIP("multipart/x-gzip", "gzip"), MULTIPART_XUSTAR_USTAR("multipart/x-ustar", "ustar"), MULTIPART_XZIP_ZIP("multipart/x-zip", "zip"),
  MUSIC_CRESCENDO_MID("music/crescendo", "mid"), MUSIC_CRESCENDO_MIDI("music/crescendo", "midi"), MUSIC_XKARAOKE_KAR("music/x-karaoke", "kar"), PALEOVU_XPV_PVU("paleovu/x-pv", "pvu"),
  TEXT_ASP_ASP("text/asp", "asp"), TEXT_ECMASCRIPT_JS("text/ecmascript", "js"), TEXT_HTML_ACGI("text/html", "acgi"), TEXT_HTML_HTM("text/html", "htm"), TEXT_HTML_HTMLS("text/html", "htmls"),
  TEXT_HTML_HTX("text/html", "htx"), TEXT_HTML_SHTML("text/html", "shtml"), TEXT_MCF_MCF("text/mcf", "mcf"), TEXT_PASCAL_PAS("text/pascal", "pas"), TEXT_PLAIN_C("text/plain", "c"),
  TEXT_PLAIN_CC("text/plain", "cc"), TEXT_PLAIN_COM("text/plain", "com"), TEXT_PLAIN_CONF("text/plain", "conf"), TEXT_PLAIN_CPLUSPLUS("text/plain", "c++"), TEXT_PLAIN_CXX("text/plain", "cxx"),
  TEXT_PLAIN_DEF("text/plain", "def"), TEXT_PLAIN_F("text/plain", "f"), TEXT_PLAIN_F90("text/plain", "f90"), TEXT_PLAIN_FOR("text/plain", "for"), TEXT_PLAIN_G("text/plain", "g"),
  TEXT_PLAIN_H("text/plain", "h"), TEXT_PLAIN_HH("text/plain", "hh"), TEXT_PLAIN_IDC("text/plain", "idc"), TEXT_PLAIN_JAV("text/plain", "jav"), TEXT_PLAIN_JAVA("text/plain", "java"),
  TEXT_PLAIN_LIST("text/plain", "list"), TEXT_PLAIN_LOG("text/plain", "log"), TEXT_PLAIN_LST("text/plain", "lst"), TEXT_PLAIN_M("text/plain", "m"), TEXT_PLAIN_MAR("text/plain", "mar"),
  TEXT_PLAIN_PL("text/plain", "pl"), TEXT_PLAIN_SDML("text/plain", "sdml"), TEXT_PLAIN_TEXT("text/plain", "text"), TEXT_RICHTEXT_RT("text/richtext", "rt"), TEXT_RICHTEXT_RTF("text/richtext", "rtf"),
  TEXT_RICHTEXT_RTX("text/richtext", "rtx"), TEXT_SCRIPLET_WSC("text/scriplet", "wsc"), TEXT_SGML_SGM("text/sgml", "sgm"), TEXT_SGML_SGML("text/sgml", "sgml"),
  TEXT_TABSEPARATEDVALUES_TSV("text/tab-separated-values", "tsv"), TEXT_URILIST_UNI("text/uri-list", "uni"), TEXT_URILIST_UNIS("text/uri-list", "unis"), TEXT_URILIST_URI("text/uri-list", "uri"),
  TEXT_URILIST_URIS("text/uri-list", "uris"), TEXT_VNDABC_ABC("text/vnd.abc", "abc"), TEXT_VNDFMIFLEXSTOR_FLX("text/vnd.fmi.flexstor", "flx"), TEXT_VNDRNREALTEXT_RT("text/vnd.rn-realtext", "rt"),
  TEXT_VNDWAPWMLSCRIPT_WMLS("text/vnd.wap.wmlscript", "wmls"), TEXT_VNDWAPWML_WML("text/vnd.wap.wml", "wml"), TEXT_WEBVIEWHTML_HTT("text/webviewhtml", "htt"), TEXT_XASM_ASM("text/x-asm", "asm"),
  TEXT_XASM_S("text/x-asm", "s"), TEXT_XAUDIOSOFTINTRA_AIP("text/x-audiosoft-intra", "aip"), TEXT_XCOMPONENT_HTC("text/x-component", "htc"), TEXT_XC_C("text/x-c", "c"), TEXT_XC_CC("text/x-c", "cc"),
  TEXT_XC_CPP("text/x-c", "cpp"), TEXT_XFORTRAN_F("text/x-fortran", "f"), TEXT_XFORTRAN_F77("text/x-fortran", "f77"), TEXT_XFORTRAN_F90("text/x-fortran", "f90"),
  TEXT_XFORTRAN_FOR("text/x-fortran", "for"), TEXT_XH_H("text/x-h", "h"), TEXT_XH_HH("text/x-h", "hh"), TEXT_XJAVASOURCE_JAV("text/x-java-source", "jav"),
  TEXT_XJAVASOURCE_JAVA("text/x-java-source", "java"), TEXT_XLAASF_LSX("text/x-la-asf", "lsx"),
  //
  TEXT_XML_XML("text/xml", "xml"), TEXT_XM_M("text/x-m", "m"), TEXT_XPASCAL_P("text/x-pascal", "p"), TEXT_XSCRIPTCSH_CSH("text/x-script.csh", "csh"), TEXT_XSCRIPTELISP_EL("text/x-script.elisp", "el"),
  TEXT_XSCRIPTGUILE_SCM("text/x-script.guile", "scm"), TEXT_XSCRIPTKSH_KSH("text/x-script.ksh", "ksh"), TEXT_XSCRIPTLISP_LSP("text/x-script.lisp", "lsp"),
  TEXT_XSCRIPTPERLMODULE_PM("text/x-script.perl-module", "pm"), TEXT_XSCRIPTPERL_PL("text/x-script.perl", "pl"), TEXT_XSCRIPTPHYTON_PY("text/x-script.phyton", "py"),
  TEXT_XSCRIPTREXX_REXX("text/x-script.rexx", "rexx"), TEXT_XSCRIPTSCHEME_SCM("text/x-script.scheme", "scm"), TEXT_XSCRIPTSH_SH("text/x-script.sh", "sh"),
  TEXT_XSCRIPTTCL_TCL("text/x-script.tcl", "tcl"), TEXT_XSCRIPTTCSH_TCSH("text/x-script.tcsh", "tcsh"), TEXT_XSCRIPTZSH_ZSH("text/x-script.zsh", "zsh"), TEXT_XSCRIPT_HLB("text/x-script", "hlb"),
  TEXT_XSERVERPARSEDHTML_SHTML("text/x-server-parsed-html", "shtml"), TEXT_XSERVERPARSEDHTML_SSI("text/x-server-parsed-html", "ssi"), TEXT_XSETEXT_ETX("text/x-setext", "etx"),
  TEXT_XSGML_SGM("text/x-sgml", "sgm"), TEXT_XSGML_SGML("text/x-sgml", "sgml"), TEXT_XSPEECH_SPC("text/x-speech", "spc"), TEXT_XSPEECH_TALK("text/x-speech", "talk"),
  TEXT_XUIL_UIL("text/x-uil", "uil"), TEXT_XUUENCODE_UU("text/x-uuencode", "uu"), TEXT_XUUENCODE_UUE("text/x-uuencode", "uue"), TEXT_XVCALENDAR_VCS("text/x-vcalendar", "vcs"),
  VIDEO_ANIMAFLEX_AFL("video/animaflex", "afl"), VIDEO_AVSVIDEO_AVS("video/avs-video", "avs"), VIDEO_DL_DL("video/dl", "dl"), VIDEO_FLI_FLI("video/fli", "fli"), VIDEO_GL_GL("video/gl", "gl"),
  VIDEO_MPEG_M1V("video/mpeg", "m1v"), VIDEO_MPEG_M2V("video/mpeg", "m2v"), VIDEO_MPEG_MP2("video/mpeg", "mp2"), VIDEO_MPEG_MP3("video/mpeg", "mp3"), VIDEO_MPEG_MPA("video/mpeg", "mpa"),
  VIDEO_MPEG_MPE("video/mpeg", "mpe"), VIDEO_MPEG_MPEG("video/mpeg", "mpeg"), VIDEO_MPEG_MPG("video/mpeg", "mpg"), VIDEO_MSVIDEO_AVI("video/msvideo", "avi"),
  VIDEO_QUICKTIME_MOOV("video/quicktime", "moov"), VIDEO_QUICKTIME_QT("video/quicktime", "qt"), VIDEO_VDO_VDO("video/vdo", "vdo"), VIDEO_VIVO_VIV("video/vivo", "viv"),
  VIDEO_VIVO_VIVO("video/vivo", "vivo"), VIDEO_VNDRNREALVIDEO_RV("video/vnd.rn-realvideo", "rv"), VIDEO_VNDVIVO_VIV("video/vnd.vivo", "viv"), VIDEO_VNDVIVO_VIVO("video/vnd.vivo", "vivo"),
  VIDEO_VOSAIC_VOS("video/vosaic", "vos"), VIDEO_XAMTDEMORUN_XDR("video/x-amt-demorun", "xdr"), VIDEO_XAMTSHOWRUN_XSR("video/x-amt-showrun", "xsr"),
  VIDEO_XATOMIC3DFEATURE_FMF("video/x-atomic3d-feature", "fmf"), VIDEO_XDL_DL("video/x-dl", "dl"), VIDEO_XDV_DIF("video/x-dv", "dif"), VIDEO_XDV_DV("video/x-dv", "dv"),
  VIDEO_XFLI_FLI("video/x-fli", "fli"), VIDEO_XGL_GL("video/x-gl", "gl"), VIDEO_XISVIDEO_ISU("video/x-isvideo", "isu"), VIDEO_XMOTIONJPEG_MJPG("video/x-motion-jpeg", "mjpg"),
  VIDEO_XMPEG_MP2("video/x-mpeg", "mp2"), VIDEO_XMPEG_MP3("video/x-mpeg", "mp3"), VIDEO_XMPEQ2A_MP2("video/x-mpeq2a", "mp2"), VIDEO_XMSASFPLUGIN_ASX("video/x-ms-asf-plugin", "asx"),
  VIDEO_XMSASF_ASF("video/x-ms-asf", "asf"), VIDEO_XMSASF_ASX("video/x-ms-asf", "asx"), VIDEO_XMSVIDEO_AVI("video/x-msvideo", "avi"), VIDEO_XQTC_QTC("video/x-qtc", "qtc"),
  VIDEO_XSCM_SCM("video/x-scm", "scm"), VIDEO_XSGIMOVIE_MOVIE("video/x-sgi-movie", "movie"), VIDEO_XSGIMOVIE_MV("video/x-sgi-movie", "mv"), WINDOWS_METAFILE_WMF("windows/metafile", "wmf"),
  WWW_MIME_MIME("www/mime", "mime"), XCONFERENCE_XCOOLTALK_ICE("x-conference/x-cooltalk", "ice"), XGL_DRAWING_XGZ("xgl/drawing", "xgz"), XGL_MOVIE_XMZ("xgl/movie", "xmz"),
  XMUSIC_XMIDI_MID("x-music/x-midi", "mid"), XMUSIC_XMIDI_MIDI("x-music/x-midi", "midi"),
  //
  XWORLD_X3DMF_3DM("x-world/x-3dmf", "3dm"), XWORLD_X3DMF_3DMF("x-world/x-3dmf", "3dmf"),
  //
  XWORLD_X3DMF_QD3("x-world/x-3dmf", "qd3"), XWORLD_X3DMF_QD3D("x-world/x-3dmf", "qd3d"),
  //
  XWORLD_XSVR_SVR("x-world/x-svr", "svr"), XWORLD_XVRML_VRML("x-world/x-vrml", "vrml"), XWORLD_XVRML_WRL("x-world/x-vrml", "wrl"),
  //
  XWORLD_XVRML_WRZ("x-world/x-vrml", "wrz"), XWORLD_XVRT_VRT("x-world/x-vrt", "vrt");

  /**
   * Gets the first matching mime-type for the given extension
   * @param extension e.g. "zip"
   * @return The MimeType that matched the given extension
   */
  public static MimeType fromExtension(String extension) {
    if (extension != null) {
      extension = extension.trim().toLowerCase();
      for (MimeType mt : MimeType.values()) {
        if (mt.getExtension().equals(extension)) {
          return mt;
        }
      }
    }

    return null;
  }

  /**
   * Gets the first matching mime-type for the given type
   * @param type e.g. "application/zip"
   * @return The MimeType that matched the given type
   */
  public static MimeType fromType(String type) {
    if (type != null) {
      type = type.trim().toLowerCase();
      for (MimeType mt : MimeType.values()) {
        if (mt.getType().equals(type)) {
          return mt;
        }
      }
    }

    return null;
  }

  private final String type;

  private final String extension;

  MimeType(String type, String extension) {
    this.type = type;
    this.extension = extension;
  }

  /**
   * Get the extension for this type. e.g. "zip"
   * @return
   */
  public String getExtension() {
    return extension;
  }

  /**
   * Get a String representation of this type. e.g. "application/zip"
   * @return
   */
  public String getType() {
    return type;
  }
}
