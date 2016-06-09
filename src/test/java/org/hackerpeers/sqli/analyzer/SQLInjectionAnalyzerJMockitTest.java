package org.hackerpeers.sqli.analyzer;

/**
 * Test methods in SQLInjectionAnalyzer class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzerJMockitTest {

//    private ExecutorService realThreadPool;
//
//    @BeforeMethod
//    public void setUp() {
//        realThreadPool = getField(SQLInjectionAnalyzer.class, "analyzerPool");
//    }
//
//    @AfterMethod
//    public void tearDown() {
//        setField(SQLInjectionAnalyzer.class, "analyzerPool", realThreadPool);
//    }
//
//    /**
//     * Test constructor, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testConstructor() {
//        Object instance = newInstance(SQLInjectionAnalyzer.class);
//        assertNotNull(instance);
//    }
//
//    /**
//     * Test of getEntryClass method, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testGetEntryClass(@Mocked final SQLIAnalyzerConfig d) {
//
//        String result = invoke(SQLInjectionAnalyzer.class, "getEntryClass");
//        assertEquals("Not found", result);
//
//        new NonStrictExpectations() {
//            {
//                SQLIAnalyzerConfig.getAnalyzerEntrypointPackages();
//                result = Arrays.asList("com");
//            }
//        };
//        invoke(SQLInjectionAnalyzer.class, "getEntryClass");
//        assertEquals("Not found", result);
//    }
//
//    /**
//     * Test of analyze method, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testAnalyze_4args_1(@Mocked final Logger log) throws InterruptedException {
//        String ln = System.getProperty("line.separator");
//        String sql = "select timestamp from dual";
//        final String expLog1 = "\tThread[main,5,main] (thread id:1) -- ============= Begin : SQL Injection Analyzer <Not found> ================= " + ln
//                + "\tThread[main,5,main] (thread id:1) -- Query total time (millisecond) : " + SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo() + " " + ln
//                + "\tThread[main,5,main] (thread id:1) -- select timestamp from dual " + ln
//                + "\tThread[main,5,main] (thread id:1) -- ============= End : SQL Injection Analyzer <Not found> ================= " + ln;
//        new Expectations() {
//
//            {
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//                times = 1;
//
//                log.isLoggable(Level.INFO);
//                result = true;
//                times = 1;
//
//                log.log(Level.INFO, expLog1);
//                times = 1;
//            }
//        };
//        SQLInjectionAnalyzer.analyze(sql, null, 0L, SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
//        Thread.sleep(100L);
//
//        final String expLog2 = "\tThread[main,5,main] (thread id:1) -- ============= Begin : SQL Injection Analyzer <Not found> ================= " + ln
//                + "\tThread[main,5,main] (thread id:1) -- Query total time (millisecond) : " + SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo() + " " + ln
//                + "\tThread[main,5,main] (thread id:1) -- select timestamp from dual " + ln
//                + "\tThread[main,5,main] (thread id:1) -- \t1 => [abc] (java.lang.String) " + ln
//                + "\tThread[main,5,main] (thread id:1) -- ============= End : SQL Injection Analyzer <Not found> ================= " + ln;
//        new Expectations() {
//
//            {
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//                times = 1;
//
//                log.isLoggable(Level.INFO);
//                result = true;
//                times = 1;
//
//                log.log(Level.INFO, expLog2);
//                times = 1;
//            }
//        };
//        Map<Object, Object> params = new HashMap<Object, Object>();
//        params.put(1, "abc");
//        SQLInjectionAnalyzer.analyze(sql, params, 0L, SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
//        Thread.sleep(100L);
//    }
//
//    /**
//     * Test of analyze method, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testAnalyze_4args_2(@Mocked final Logger log) throws InterruptedException {
//        String ln = System.getProperty("line.separator");
//        String sql = "select timestamp from dual";
//        final String expLog1 = "\tThread[main,5,main] (thread id:1) -- ============= Begin : SQL Injection Analyzer <Not found> ================= " + ln
//                + "\tThread[main,5,main] (thread id:1) -- Query total time (millisecond) : " + SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo() + " " + ln
//                + "\tThread[main,5,main] (thread id:1) -- select timestamp from dual " + ln
//                + "\tThread[main,5,main] (thread id:1) -- ============= End : SQL Injection Analyzer <Not found> ================= " + ln;
//        new Expectations() {
//
//            {
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//                times = 1;
//
//                log.isLoggable(Level.INFO);
//                result = true;
//                times = 1;
//
//                log.log(Level.INFO, expLog1);
//                times = 1;
//            }
//        };
//        SQLInjectionAnalyzer.analyze(Arrays.asList(sql), null, 0, SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
//        Thread.sleep(100L);
//
//        final String expLog2 = "\tThread[main,5,main] (thread id:1) -- ============= Begin : SQL Injection Analyzer <Not found> ================= " + ln
//                + "\tThread[main,5,main] (thread id:1) -- Query total time (millisecond) : " + SQLIAnalyzerConfig.getAnalyzerLogThresholdFine() + " " + ln
//                + "\tThread[main,5,main] (thread id:1) -- select timestamp from dual " + ln
//                + "\tThread[main,5,main] (thread id:1) -- \t1 => [null] (unknown) " + ln
//                + "\tThread[main,5,main] (thread id:1) -- ============= End : SQL Injection Analyzer <Not found> ================= " + ln;
//        new Expectations() {
//
//            {
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//                times = 1;
//
//                log.isLoggable(Level.FINE);
//                result = true;
//                times = 1;
//
//                log.log(Level.FINE, expLog2);
//                times = 1;
//            }
//        };
//        Map<Object, Object> params = new HashMap<Object, Object>();
//        params.put(1, null);
//        SQLInjectionAnalyzer.analyze(Arrays.asList(sql), Arrays.asList(params), 0L, SQLIAnalyzerConfig.getAnalyzerLogThresholdFine());
//        Thread.sleep(100L);
//    }
//
//    /**
//     * Test of submitThread method, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testSubmitThread() throws InterruptedException {
//        String ln = System.getProperty("line.separator");
//        String sql = "select timestamp from dual";
//        final String expLog1 = "\tThread[main,5,main] (thread id:1) -- ============= Begin : SQL Injection Analyzer <Not found> ================= " + ln
//                + "\tThread[main,5,main] (thread id:1) -- Query total time (millisecond) : " + SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo() + " " + ln
//                + "\tThread[main,5,main] (thread id:1) -- select timestamp from dual " + ln
//                + "\tThread[main,5,main] (thread id:1) -- ============= End : SQL Injection Analyzer <Not found> ================= " + ln;
//
//        SQLInjectionAnalyzer.analyze(Arrays.asList(sql), null, 0L, SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
//
//        final String expLog2 = "\tThread[main,5,main] (thread id:1) -- ============= Begin : SQL Injection Analyzer <JUnit> ================= " + ln
//                + "\tThread[main,5,main] (thread id:1) -- Query total time (millisecond) : " + SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo() + " " + ln
//                + "\tThread[main,5,main] (thread id:1) -- select timestamp from dual " + ln
//                + "\tThread[main,5,main] (thread id:1) -- \t1 => [null] (unknown) " + ln
//                + "\tThread[main,5,main] (thread id:1) -- ============= End : SQL Injection Analyzer <JUnit> ================= " + ln;
//
//        Map<Integer, Object> params = new HashMap<Integer, Object>();
//        params.put(1, null);
//        invoke(SQLInjectionAnalyzer.class, "submitThread", "JUnit", Arrays.asList(sql), Arrays.asList(params), 0L, SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
//        Thread.sleep(100L);
//        invoke(SQLInjectionAnalyzer.class, "submitThread", "JUnit", Arrays.asList(sql), Arrays.asList(params), 0L, SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
//        Thread.sleep(100L);
//    }
//
//    /**
//     * Test of testShutdown method, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testShutdown(@Mocked final ExecutorService executorService,
//                             @Mocked final Logger log,
//                             @Mocked final SQLInjectionAnalyzerRunnable runnable) throws IOException {
//        setField(SQLInjectionAnalyzer.class, "analyzerPool", executorService);
//        new Expectations() {
//            {
//                executorService.shutdown();
//                times = 1;
//
//                SQLInjectionAnalyzerRunnable.storeEntriesToDisk();
//                times = 1;
//
//                executorService.shutdown();
//                times = 1;
//
//                SQLInjectionAnalyzerRunnable.storeEntriesToDisk();
//                result = new IOException("Generate a log of this error");
//                times = 1;
//
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//                times = 1;
//
//                log.log(Level.SEVERE, "Unable to store result on disk", (IOException) any);
//                times = 1;
//            }
//        };
//
//        SQLInjectionAnalyzer.shutdown();
//        SQLInjectionAnalyzer.shutdown();
//
//    }
//
//    /**
//     * Test of testShutdownNow method, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testShutdownNow(@Mocked final ExecutorService executorService,
//                                @Mocked final Logger log,
//                                @Mocked final SQLInjectionAnalyzerRunnable runnable) throws IOException {
//        setField(SQLInjectionAnalyzer.class, "analyzerPool", executorService);
//        new Expectations() {
//
//            {
//                executorService.shutdownNow();
//                times = 1;
//
//                SQLInjectionAnalyzerRunnable.storeEntriesToDisk();
//                times = 1;
//
//                executorService.shutdownNow();
//                times = 1;
//
//                SQLInjectionAnalyzerRunnable.storeEntriesToDisk();
//                result = new IOException("Generate a log of this error");
//                times = 1;
//
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//                times = 1;
//
//                log.log(Level.SEVERE, "Unable to store result on disk", (IOException) any);
//                times = 1;
//            }
//        };
//
//        SQLInjectionAnalyzer.shutdownNow();
//        SQLInjectionAnalyzer.shutdownNow();
//    }
//
//
//    /**
//     * Test of getAllEntries method, of class SQLInjectionAnalyzer.
//     */
//    @Test
//    public void testGetAllEntries(@Mocked final SQLInjectionAnalyzerRunnable runnable) throws Exception {
//
//       new Expectations() {
//
//            {
//                SQLInjectionAnalyzerRunnable.getEntries();
//                times = 1;
//            }
//       };
//       SQLInjectionAnalyzer.getAllEntries();
//    }
}
