import re
import os
from py2neo import Node, Relationship, Graph, NodeMatcher, RelationshipMatcher

test_graph = Graph('http://localhost:7474', auth=('neo4j', 'Czy026110'), name='neo4j')

# query = """
# MATCH (n)
# DETACH DELETE n
# """
# test_graph.run(query)


name_list = ["顾凌峰","张若皓","王明俊","徐王子","李泽雨"]
directory_root_path = '/Users/seedoilz/Downloads/总/'

count = 0
pattern = r'\[@(\w+)#(\w+)\*\]'
for name in name_list:
    directory_path = directory_root_path + name
    for folder, subfolders, files in os.walk(directory_path):
        for file in files:
            if file.endswith("ann"):
                file_path = os.path.join(folder, file)
                print(f'文件路径：{file_path}')
                print(count)
                with open(file_path) as f:
                    count += 1
                    if count < 807:
                        continue
                    file_content = f.read()
                    matches = re.findall(pattern, file_content)
                    entity = ""
                    sentiments = []
                    advantages = []
                    disadvantages = []
                    uses = []

                    # 解析数据
                    for match in matches:
                        variable_part = str(match[0])  # 匹配 [@VARY#entity*] 中的 VARY 部分
                        annotation = str(match[1])  # 匹配 [@VARY#entity*] 中的 Annotation 部分
                        # print(f"Variable part: {variable_part}, Annotation: {annotation}")
                        if annotation.lower() == "entity":
                            entity = variable_part
                        elif annotation.lower() == "sentiment":
                            sentiments.append(variable_part)
                        elif annotation.lower() == "advantage":
                            advantages.append(variable_part)
                        elif annotation.lower() == "disadvantage":
                            disadvantages.append(variable_part)
                        elif annotation.lower() == "use":
                            uses.append(variable_part)

                    # 填充数据到数据库中
                    entity_node = Node("Entity", name=entity)
                    test_graph.create(entity_node)
                    for sentiment in sentiments:
                        sentiment_node = Node("Sentiment", name=sentiment)
                        test_graph.create(sentiment_node)
                        test_graph.create(Relationship(sentiment_node, "下属于", entity_node))
                    for advantage in advantages:
                        advantage_node = Node("Advantage", name=advantage)
                        test_graph.create(advantage_node)
                        test_graph.create(Relationship(advantage_node, "下属于", entity_node))
                    for disadvantage in disadvantages:
                        disadvantage_node = Node("Disadvantage", name=disadvantage)
                        test_graph.create(disadvantage_node)
                        test_graph.create(Relationship(disadvantage_node, "下属于", entity_node))
                    for use in uses:
                        use_node = Node("Use", name=use)
                        test_graph.create(use_node)
                        test_graph.create(Relationship(use_node, "下属于", entity_node))
                f.close()