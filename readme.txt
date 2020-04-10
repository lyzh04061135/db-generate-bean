1、根据数据表生成实体pojo
2、根据实体生成数据表
3、可以使用自定义注解完善
4、适用数据库达梦数据库
5.springboot 配置日期格式
spring:
  jackson:
    time-zone: GMT+8
    date-format: yyyy-MM-dd hh:mm:ss
6、jackson处理嵌套list
@Test
public void testDateFormat2() throws IOException {
    ArrayList<RequestList> requestLists = Lists.newArrayList(new RequestList(Lists.newArrayList(new Apartment("Joey", "Chandler")),1), new RequestList(Lists.newArrayList(new Apartment("Joey", "Chandler")),2));
    ObjectMapper objectMapper = new ObjectMapper();
    String string = objectMapper.writeValueAsString(requestLists);
    System.out.println(string);
    List<RequestList> m =  new ObjectMapper().readValue(string, new TypeReference<List<RequestList>>(){});
    assertEquals(m.size(),2);
}

class RequestList
{
    private List<Apartment> apartment;

    private Integer id;

    public RequestList() {
    }

    public RequestList(List<Apartment> apartment, Integer id) {
        this.apartment = apartment;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Apartment> getApartment() {
        return apartment;
    }

    public void setApartment(List<Apartment> apartment) {
        this.apartment = apartment;
    }
}

7、事务分批提交
  @Autowired
    PlatformTransactionManager transactionManager;

    public void test() {
        testTransaction(false);
        testTransaction(true);
    }

    public void testTransaction(boolean flag) {
        List<InterfaceUser>list=new ArrayList<>();
        InterfaceUser interfaceUser=new InterfaceUser();
        list.add(interfaceUser);
        interfaceUser.setInterfaceUserId(CommonUtil.getUuid());
        interfaceUser.setUserId("1000");
        interfaceUser.setInterfaceId("1000");

        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status= transactionManager.getTransaction(definition);

        try {
            interfaceUserDao.insertBatch(list);

            if (flag) {
                int i=1/0;
            }

            interfaceUser.setInterfaceUserId(CommonUtil.getUuid());
            interfaceUserDao.insertBatch(list);
            transactionManager.commit(status);
        } catch (Exception e) {
            logger.error("", e);
            status.setRollbackOnly();
        }
    }
