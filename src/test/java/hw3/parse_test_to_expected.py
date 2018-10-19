

def split_file():
    import os
    # current folder should only have to be splitted *.test files, all others should be remvoed.
    file_names = [file for file in os.listdir('original/') if file.endswith(".test")]
    for file_name in file_names:
        count = 0
        with open('original/' + file_name) as file:
            content = file.read()
        content = content.splitlines()
        while ''.join(content):

            start = content.index("# START_TEST")
            end = content.index("# END_TEST")  + 1 # In python indexing: end index is exclusive & start index is inclusive.
            cur_content = content[start : end]
            content_to_write = '\n'.join(cur_content)
            temp = cur_content[1].split()

            if temp[0] == '#' and temp[1] == 'NAME':
                branch = temp[2]
            else:
                branch = count
                count += 1
            with open("{}_{}{}".format(os.path.splitext(file_name)[0], branch, ".test"), mode = 'w+') as file:
                file.write(content_to_write)

            content = content[end:]



def create_expect_file():
    import os
    # current folder should only have to be parsed *.test files, all others should be remvoed.
    file_names = [file for file in os.listdir() if file.endswith(".test")]
    for file_name in file_names:
        with open(file_name) as file:
            content = file.read()
        result = parse(content)
        with open(os.path.splitext(file_name)[0] + ".expected", mode='w+') as file:
            file.write(result)


# input should be the raw input read from the file as a single string
def parse(content):
    # graph = key: name, value = (Node, Edge)
    # Edge = [start, end, cost/label]
    # Node = "content"
    lines = content.splitlines()
    result = []
    graphs = dict()
    for line in lines:
        if line.startswith("#") or not line:
            result.append(line)
        else:
            line_split = line.split()
            if line_split[0] == 'CreateGraph':
                assert len(line_split) == 2

                graph_name = line_split[1]
                line_to_write = 'created graph {}'.format(graph_name)
                if graph_name in graphs:
                    line_to_write = 'duplicate graph detected!'
                else:
                    graphs[graph_name] = ([], [])


            elif line_split[0] == 'AddNode':
                assert len(line_split) == 3

                graph_name = line_split[1]
                node_data = line_split[2]

                line_to_write = 'added node {} to {}'.format(node_data, graph_name)

                if graph_name not in graphs:
                    line_to_write = 'Please create graph at first.'
                else:
                    if node_data in graphs[graph_name][0]:
                        line_to_write = 'Duplicate Node detected!'
                    else:
                        graphs[graph_name][0].append(node_data)

            elif line_split[0] == 'AddEdge':
                assert len(line_split) == 5

                graph_name = line_split[1]
                parent_node = line_split[2]
                children_node = line_split[3]
                edge_label = line_split[4]

                line_to_write = 'added edge {} from {} to {} in {}'.format(edge_label, parent_node, children_node, graph_name)

                if graph_name not in graphs:
                    line_to_write = 'Please create graph at first.'
                else:
                    # either node is absent
                    if parent_node not in graphs[graph_name][0] or children_node not in graphs[graph_name][0]:
                        line_to_write = 'At least one node of the edge is not in the graph!'
                    else:
                        edge = [parent_node, children_node, edge_label]
                        # duplicate edge
                        if edge in graphs[graph_name][1]:
                            line_to_write = 'Duplicate Edge detected.'
                        else:
                            graphs[graph_name][1].append(edge)
            elif line_split[0] == 'ListNodes':
                assert len(line_split) == 2

                graph_name = line_split[1]

                if graph_name not in graphs:
                    line_to_write = 'Please create graph at first.'
                else:
                    node_list = []
                    for node in graphs[graph_name][0]:
                        node_list.append(" {}".format(node))
                    node_list.sort()
                    line_to_write = '{} contains:{}'.format(graph_name, "".join(node_list))
            elif line_split[0] == 'ListChildren':
                assert len(line_split) == 3

                graph_name = line_split[1]
                parent_node = line_split[2]

                if graph_name not in graphs:
                    line_to_write = 'Please create graph at first.'
                else:
                    if parent_node not in graphs[graph_name][0]:
                        line_to_write = 'Node is not in graph'
                    else:
                        edge_list = []
                        for edge in graphs[graph_name][1]:
                            if edge[0] == parent_node:
                                edge_list.append(edge)
                        # first sort on first element, which is children node
                        # then sort on the last element , which is edgelabel.
                        edge_list.sort(key = lambda x : (x[1], x[-1]))

                        new_edge_list = [" {}({})".format(edge[1], edge[-1]) for edge in edge_list]
                        line_to_write = 'the children of {} in {} are:{}'.format(parent_node, graph_name, "".join(new_edge_list))
            else:
                raise Exception("Input is not specified, plz check: {}".format(line))

            result.append(line_to_write)

    return "\n".join(result)

if __name__ == '__main__':
    split_file()
    create_expect_file()